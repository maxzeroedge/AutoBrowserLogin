
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * A {@link X509TrustManager} and {@link HostnameVerifier} which trust everything.
 * 
 * @author    Torleif Berger
 * @license   http://creativecommons.org/licenses/by/3.0/
 * @see       http://www.geekality.net/?p=2408
 */
final class TrustAllCertificates implements X509TrustManager, HostnameVerifier
{
    public X509Certificate[] getAcceptedIssuers() {return null;}
    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    public boolean verify(String hostname, SSLSession session) {return true;}
    
    /**
     * Installs a new {@link TrustAllCertificates} as trust manager and hostname verifier. 
     */
    public void install()
    {
        try
        {
            TrustAllCertificates trustAll = new TrustAllCertificates();
            
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, 
                    new TrustManager[]{trustAll}, 
                    new java.security.SecureRandom());          
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(trustAll);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Failed setting up all thrusting certificate manager.", e);
        }
        catch (KeyManagementException e)
        {
            throw new RuntimeException("Failed setting up all thrusting certificate manager.", e);
        }
    }
}

public class AutoBrowserLogin {
        
    /**
     * Creates a new instance of <code>Autologin</code>.
     */
    public AutoBrowserLogin() {
    }
    
    private final String USER_AGENT = "Mozilla/5.0";
    
    public String fun2() throws Exception{
        new TrustAllCertificates().install();
        String url = "https://1.1.1.1/login.html";

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "buttonClicked=4&err_flag=0&err_msg=&info_flag=0&info_msg=&redirect_url=&network_name=IIT&username=jawahar&password=Jawahar%40123";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParameters);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            //System.out.println(response.toString());
           return "Connected\n"+response.toString();
    }
    
    public String fun3() throws Exception{
    	try{
    		if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0){
    			//Windows
    			Runtime.getRuntime().exec("netsh wlan connect name=IIT").waitFor();
    		}
    		else if(System.getProperty("os.name").toLowerCase().indexOf("nix") >= 0 || System.getProperty("os.name").toLowerCase().indexOf("nux") >= 0 || System.getProperty("os.name").toLowerCase().indexOf("aix") > 0 ){
    			//Unix
    			Runtime.getRuntime().exec("iwconfig wlan0 essid IIT").waitFor();
    		}
    		else if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
    			//Mac
    			Runtime.getRuntime().exec("networksetup -setairportpower en0 on").waitFor();
    			Runtime.getRuntime().exec("/System/Library/PrivateFrameworks/Apple80211.framework/Versions/A/Resources/airport scan").waitFor();
    			Runtime.getRuntime().exec("networksetup -setairportnetwork en0 IIT").waitFor();
    		}
    		else if(System.getProperty("os.name").toLowerCase().indexOf("sunos")>=0){
    			//Solaris
    		}
    	}
    	catch(Exception e){
    		return e.getStackTrace().toString();
    	}
    	return fun2();
    }
    
    public static void main(String[] args) throws Exception{
    	if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
    	final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(new ImageIcon(new AutoBrowserLogin().getClass().getClassLoader().getResource("images/ic_launcher.gif")).getImage(), "tray icon");
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem loginConnectItem = new MenuItem("Attempt Connection and Login");
        MenuItem loginItem = new MenuItem("Attempt Login");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Exit");
       
        //Add action to menus
        ActionListener actListLoginConnect = new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		try{
        			JOptionPane.showMessageDialog(null, new AutoBrowserLogin().fun3(), "Process Completed", JOptionPane.INFORMATION_MESSAGE);
        		}
        		catch(Exception ex){
        			ex.printStackTrace();
        		}
        	}
        };
        ActionListener actListLogin = new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		try{
        			JOptionPane.showMessageDialog(null, new AutoBrowserLogin().fun2(), "Process Completed", JOptionPane.INFORMATION_MESSAGE);
        		}
        		catch(Exception ex){
        			ex.printStackTrace();
        		}
        	}
        };
        ActionListener actListAbt = new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		String infoMessage = "Is one really needed?";
        		JOptionPane.showMessageDialog(null, infoMessage, "About Me", JOptionPane.INFORMATION_MESSAGE);
        	}
        };
        ActionListener actListExit = new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		System.exit(0);
        	}
        };
        loginConnectItem.addActionListener(actListLoginConnect);
        loginItem.addActionListener(actListLogin);
        aboutItem.addActionListener(actListAbt);
        exitItem.addActionListener(actListExit);
        
        //Add components to pop-up menu
        popup.add(loginConnectItem);
        popup.add(loginItem);
        popup.add(aboutItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
    
}


