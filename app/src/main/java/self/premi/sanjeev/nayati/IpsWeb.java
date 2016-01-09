/*
 * [Nayati] IpsWeb.java
 *
 * Implements steps to get tracking information via HTTP/GET request from IPS Web tracking.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Utility class to get tracking information
 */
public class IpsWeb {
    /**
     * Instance of URL connection via HTTP
     */
    private HttpURLConnection conn = null;

    /**
     * Base url of IPS Web Tracking
     */
    final String URL_IPSWEB = "http://ipsweb.ptcmysore.gov.in/ipswebtracking/IPSWeb_item_events.asp";

    /**
     * Request parameter - itemid
     */
    final String PARAM_ITEMID = "itemid";

    /**
     * Request parameter - Submit
     */
    final String PARAM_SUBMIT = "Submit";

    /**
     * Token - Start of body
     */
    final String BODY_START = "<body";

    /**
     * Token - End of body
     */
    final String BODY_END = "</body>";


    /**
     * Makes an HTTP request using GET method to the specified URL.
     *
     * @param trackNum  Tracking number
     *
     * @return An HttpURLConnection object
     */
    public boolean request(String trackNum) {
        boolean ret = false;

        //
        // Build URI for the request
        //
        Uri uri = Uri.parse(URL_IPSWEB)
                        .buildUpon()
                        .appendQueryParameter(PARAM_ITEMID, trackNum)
                        .appendQueryParameter(PARAM_SUBMIT, PARAM_SUBMIT)
                        .build();

        try {
            URL url = new URL(uri.toString());

            conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setRequestMethod("GET");

            ret = true;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


    /**
     * Returns server response as string
     *
     * @return  An array of Strings of the server's response
     */
    public String read() {

        if (conn == null) return "";

        StringBuilder sb = new StringBuilder();

        try {
           if (conn.getResponseCode() == 200) {
               //
               // Open stream to read contents of the web page
               //
               InputStream is = conn.getInputStream();

               BufferedReader br = new BufferedReader(new InputStreamReader(is));

               String line;

               while ((line = br.readLine()) != null) {
                   sb.append(line.trim());
               }

               br.close();

               //
               // Keep only the 'body' of web page received
               //
               int posBodyStart = sb.indexOf(BODY_START);
               int posBodyEnd   = sb.indexOf(BODY_END);

               if ((posBodyStart > 0) && (posBodyEnd > 0)) {
                   sb.delete(0, posBodyStart);

                   posBodyEnd = sb.indexOf(BODY_END);
                   sb.delete(posBodyEnd + BODY_END.length(), sb.length());
               } else {
                   sb.delete(0, sb.length());
               }
           }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
