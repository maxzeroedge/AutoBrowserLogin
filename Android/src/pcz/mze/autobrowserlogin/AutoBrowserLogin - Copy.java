package pcz.mze.autobrowserlogin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.*;

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

class AutoBrowserLogin {
        
    /**
     * Creates a new instance of <code>Autologin</code>.
     */
    public AutoBrowserLogin() {
    }
    
    private final String USER_AGENT = "Mozilla/5.0";
    
    public void fun2() throws Exception{
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

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
    }
    
}


