import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class NetFlow {
	List<Data> allData = new ArrayList<>(); 
	public static class Data
	{
		Date date;
		long bytesIn, bytesOut;
	    int  packetsIn, packetsOut;
	    String applicationName,destinationAdress, sourceAdress,protocolNumber;
	    public Data(Date date, long bytesIn,long bytesOut,int packetsIn, int packetsOut,String applicationName, String destinationAdress, String protocolNumber,String sourceAdress)
	    {
	    	this.date=date;
	    	this.bytesIn=bytesIn;
	    	this.bytesOut = bytesOut;
	    	this.packetsIn=packetsIn;
	    	this.packetsOut=packetsOut;
	    	this.applicationName=applicationName;
	    	this.destinationAdress=destinationAdress;
	    	this.protocolNumber=protocolNumber;
	    	this.sourceAdress=sourceAdress;
	    }
	    public long getBytesIn()
	    {
	    	return bytesIn;
	    }
	    public long getBytesOut()
	    {
	    	return bytesOut;
	    }
	    public int getPacketsIn()
	    {
	    	return packetsIn;
	    }
	    public int getPacketsOut()
	    {
	    	return packetsOut;
	    }
	}
    
    public NetFlow(String name) throws FileNotFoundException, ParseException
    {
    	 @SuppressWarnings("resource")
		 Scanner scanner = new Scanner(new File(name));
         List<String> list = new ArrayList<>();
         while(scanner.hasNextLine())
         {
        	 list.add(scanner.nextLine());
         }
         list.remove(0);
         for(String s:list)
         {
        	 String[] tmp = s.split(",");
        	 Date tmpDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS aa").parse(tmp[0]);
        	 long tmpBytesIn =Long.parseLong(tmp[1]);
        	 long tmpBytesOut =Long.parseLong(tmp[2]);
        	 int tmpPacketsIn = Integer.parseInt(tmp[3]);
        	 int tmpPacketsOut = Integer.parseInt(tmp[4]);
        	 String tmpApplicationName = tmp[5];
        	 String tmpDestinationAdress = tmp[6];
        	 String tmpProtocolNumber = tmp[7];
        	 String tmpSourceAdress = tmp[8];
        	 allData.add(new Data(tmpDate,tmpBytesIn,tmpBytesOut,tmpPacketsIn,tmpPacketsOut,tmpApplicationName,tmpDestinationAdress,tmpProtocolNumber,tmpSourceAdress));
         }
       
    }
}
