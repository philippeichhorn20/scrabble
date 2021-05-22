package backend.network.server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ServerSettings {
  public static final int port = 8421;
  /**
   * @author
   * aus Stackoverflow
   */
  public static String getLocalHostIp4Address()  {
    String hostIP = "";
    try {

      InetAddress candidateAddress = null;
      for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
          ifaces.hasMoreElements();) {

        NetworkInterface iface = ifaces.nextElement();

        for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
            inetAddrs.hasMoreElements();) {

          InetAddress inetAddr = inetAddrs.nextElement();

          if (!inetAddr.isLoopbackAddress() && inetAddr instanceof Inet4Address) {
            if (inetAddr.isSiteLocalAddress()) {
              return inetAddr.getHostAddress();
            } else if (candidateAddress == null) {
              candidateAddress = inetAddr;
            }
          }
        }
      }
      if (candidateAddress != null) {
        return candidateAddress.getHostAddress();
      }

      InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
      if (jdkSuppliedAddress == null) {
        throw new UnknownHostException(
            "The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
      }
      return jdkSuppliedAddress.getHostAddress();
    } catch (Exception e) {
      //UnknownHostException unknownHostException =
      //new UnknownHostException("Failed to determine LAN address: " + e);
      //unknownHostException.initCause(e);
      //throw unknownHostException;
      hostIP = "unknown (failed to determine LAN address)";
    }
    return hostIP;
  }
}

