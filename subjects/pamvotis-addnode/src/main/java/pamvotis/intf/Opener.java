///////////////////////////////////////////////////////////////////////////////////
//Opens a file with a specified application. The file could be a url or a text file.
//Actually, it executes a command related to the current operating system.
//Extends thread because Linux opens the application (e.g. firefox) in the same thread.
//This causes the Pamvotis application to freeze, until the user closes the application (e.g. firefox).
//The method(s) that open the file in an application are created by Rachel Vecchitto.
/////////////////////////////////////////////////////////////////////////////////////////////
package pamvotis.intf;
import java.io.IOException;

	class Opener extends Thread{
		private char type='u';
		private String file=null;
		
		//Get the type arg which specifies if the file is a url of a text result file
		//Get the file to be opened
		public Opener(char type, String file){
			this.type =type;
			this.file = file;
		}
		//Calls the appropriate method
		public void run(){
			try {
				if(type=='u') openUrl(file);
				else openTxt(file);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	     public void openUrl(String url) throws InterruptedException, IOException
	     {
	         String cmd = null; 
	         if (isWindows())
	         {
	             cmd = ("rundll32 url.dll,FileProtocolHandler " + maybeFixupURLForWindows(url));
	             Runtime.getRuntime().exec(cmd);
	         }
	         else {
	             cmd = "firefox " + url;
	             Process p = Runtime.getRuntime().exec(cmd);
	             int exitcode = p.waitFor();
	             if (exitcode != 0) {
	                 cmd = "netscape " + url;
	                 Runtime.getRuntime().exec(cmd);
	             }
	         }
	     }
	     
	     public void openTxt(String txt) throws IOException, InterruptedException{
	         String cmd = null; 
	         if (isWindows())
	         {
	             cmd = "notepad " + txt;
	             Runtime.getRuntime().exec(cmd);
	         }
	         else {
	             cmd = "gedit " + txt;
	             Process p = Runtime.getRuntime().exec(cmd);
	             int exitcode = p.waitFor();
	             if (exitcode != 0) openUrl(txt);
	         }
	     }
	    /** 
	     * If the default browser is Internet Explorer 5.0 or greater, 
	     * the URL.DLL program fails if the url ends with .htm or .html . 
	     * This problem is described by Microsoft at 
	     * http://support.microsoft.com/support/kb/articles/Q283/2/25.ASP 
	     * Of course, their suggested workaround is to use the classes from the 
	     * microsoft Java SDK, but fortunately another workaround does exist. 
	     * If you alter the url slightly so it no longer ends with ".htm", 
	     * the URL can launch successfully. The logic here appends a null query 
	     * string onto the end of the URL if none is already present, or 
	     * a bogus query parameter if there is already a query string ending in 
	     * ".htm" 
	     */ 
	    private String maybeFixupURLForWindows(String url)
	    { 
	        // plain filenames (e.g. c:\some_file.html or \\server\filename) do
	        // not need fixing. 
	        if (url == null || url.length() < 2 || url.charAt(0) == '\\' || url.charAt(1) == ':') 
	            return url; 
	        String lower_url = url.toLowerCase();
	        int i = badEndings.length;
	        while (i-- > 0)
	            if (lower_url.endsWith(badEndings[i]))
	                return fixupURLForWindows(url); 
	            return url; 
	    }  
	    final String[] badEndings = { ".htm", ".html", ".htw", ".mht", ".cdf", ".mhtml", ".stm" };
	        
	    private String fixupURLForWindows(String url) {
	        if (url.indexOf('?') == -1)
	            return url + "?";
	        else return url + "&workaroundStupidWindowsBug";
	    } 
	    /**
	     * Checks if the OS is windows.
	     *
	     * @return true if it is, false if it's not.
	     */
	    private boolean isWindows()
	    {
	        if (System.getProperty("os.name").indexOf("Windows") != -1)
	        { return true; } 
	        else { return false; } 
	    }
	} 