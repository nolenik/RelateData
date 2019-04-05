import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;


public class Stat {

	
	
	public static List<String> findTopBytesIn (NetFlow nf)
	{
		List<NetFlow.Data> tmpResult = new ArrayList<>();
		HashMap<String, Long> hash = new HashMap<>();
		for (NetFlow.Data tmp:nf.allData)
		{
			if(!hash.containsKey(tmp.sourceAdress))
				hash.put(tmp.sourceAdress,  0L);
			hash.put(tmp.sourceAdress, hash.get(tmp.sourceAdress)+tmp.bytesIn);
		}
		Set<String> users = hash.keySet();
		for(String str:users)
			tmpResult.add(new NetFlow.Data(null, hash.get(str).longValue(), 0, 0, 0, null,null,null,str));
		tmpResult.sort(Comparator.comparingLong(NetFlow.Data::getBytesIn).reversed());
		tmpResult= tmpResult.subList(0, 10);
		List<String> result = new ArrayList<>();
		result.add("bytesIn  sourceAdress");
		for (NetFlow.Data o:tmpResult)
		{
			String s="";
			s=+o.bytesIn+" "+o.sourceAdress;
			result.add(s);
		}
		return result;
	}
	
	public static List<String> findTopBytesOut (NetFlow nf)
	{
		List<NetFlow.Data> tmpResult = new ArrayList<>();
		HashMap<String, Long> hash = new HashMap<>();
		for (NetFlow.Data tmp:nf.allData)
		{
			if(!hash.containsKey(tmp.sourceAdress))
				hash.put(tmp.sourceAdress,  0L);
			hash.put(tmp.sourceAdress, hash.get(tmp.sourceAdress)+tmp.bytesOut);
		}
		Set<String> users = hash.keySet();
		for(String str:users)
			tmpResult.add(new NetFlow.Data(null,0, hash.get(str).longValue(), 0, 0, null,null,null,str));
		tmpResult.sort(Comparator.comparingLong(NetFlow.Data::getBytesOut).reversed());
		tmpResult=tmpResult.subList(0, 10);
		List<String> result = new ArrayList<>();
		result.add("bytesOut  sourceAdress");
		for (NetFlow.Data o:tmpResult)
		{
			String s="";
			s=+o.bytesOut+" "+o.sourceAdress;
			result.add(s);
		}
		return result;
		
	}
	
	public static List<String> findTopProtocols(NetFlow nf)
	{
		List<String> topProtocols = new ArrayList<>();
		class Prot 
		{
			String protocol;
			int count=0;;
			Prot(String protocol)
			{
				this.protocol=protocol;
			}
			public int getCount()
			{
				return count;
			}
		}
		HashSet<String> uniqProtocols = new HashSet<>();
		for (NetFlow.Data tmpNF:nf.allData)
		{
			if(!tmpNF.protocolNumber.equals(""))
			    uniqProtocols.add(tmpNF.protocolNumber);
		}
		List<Prot> list = new ArrayList<>();
		for(String tmp:uniqProtocols)
		    list.add(new Prot(tmp));
		for (Prot p:list)
			for (NetFlow.Data tmpNF: nf.allData)
				if (p.protocol.equals(tmpNF.protocolNumber))
					p.count++;
		list.sort(Comparator.comparing(Prot::getCount).reversed());
		for(Prot i:list)
			topProtocols.add(i.protocol);
		return topProtocols.subList(0, 3);
	
	}
	
	public static List<String> findTopApplications(NetFlow nf)
	{
		List<String> topApplications = new ArrayList<>();
		class App
		{
			String app;
			int count=0;;
			App(String app)
			{
				this.app=app;
			}
			public int getCount()
			{
				return count;
			}
		}
		HashSet<String> uniqApplications = new HashSet<>();
		for (NetFlow.Data tmpNF:nf.allData)
		{
			if (!tmpNF.applicationName.equals(""))
			    uniqApplications.add(tmpNF.applicationName);
		}
		List<App> list = new ArrayList<>();
		for(String tmp:uniqApplications)
		    list.add(new App(tmp));
		for (App p:list)
			for (NetFlow.Data tmpNF: nf.allData)
				if (p.app.equals(tmpNF.applicationName))
					p.count++;
		list.sort(Comparator.comparing(App::getCount).reversed());
		for(App i:list)
			topApplications.add(i.app);
		return topApplications.subList(0, 10);
	
	}
	
	public static List<NetFlow.Data> findDate (NetFlow nf, String dateFrom, String dateTo) throws ParseException, JsonIOException, IOException
	{
		try
		{
		List<NetFlow.Data> result = new ArrayList<>();
		Date tmpDateFrom = new SimpleDateFormat("MM/dd/yyyy").parse(dateFrom);
		Date tmpDateTo = new SimpleDateFormat("MM/dd/yyyy").parse(dateTo);
		for (NetFlow.Data tmpNF:nf.allData)
			if(tmpNF.date.after(tmpDateFrom) && tmpNF.date.before(tmpDateTo))
				result.add(tmpNF);
		
		
		//Write to JSON 
		Gson gson = new Gson();
		gson.toJson(result, new FileWriter("results/findDate.json"));
		return result;
		}
		catch (ParseException e) {
			System.out.print("Invalid time");
			return null;
		}
	}
	
	public static void out(String name, List<String> l) throws IOException {
		FileWriter writer = new FileWriter(name); 
		for(String str: l) {
		  writer.write(str+"\n");
		}
		writer.close();
	}
	public static void main(String[] args) throws ParseException, IOException {
		// TODO Auto-generated method stub
		
		NetFlow a = new NetFlow("data.csv");
		File theDir = new File("results");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		        theDir.mkdir();  
		}
		out("results/topReceivers.txt",findTopBytesIn(a));
		out("results/topTransmitters.txt",findTopBytesOut(a));
		out("results/topProtocols.txt",findTopProtocols(a));
		out("results/topApplications.txt",findTopApplications(a));
		findDate(a,"7/16/2018","7/18/2018");
		SwingUtilities.invokeLater(() -> {
		      Graph example = new Graph(findTopBytesIn(a).get(1).split(" ")[1],findTopBytesOut(a).get(1).split(" ")[1],a.allData);
		      example.setAlwaysOnTop(true);
		      example.pack();
		      example.setSize(1024, 760);
		      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		      example.setVisible(true);
		    });
		
		
	}
	
}
