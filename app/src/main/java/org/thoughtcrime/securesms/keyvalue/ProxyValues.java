package org.thoughtcrime.securesms.keyvalue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.whispersystems.signalservice.internal.configuration.SignalProxy;

public final class ProxyValues extends SignalStoreValues {

  private static final String  KEY_PROXY_ENABLED = "proxy.enabled";
  private static final String  KEY_HOST          = "proxy.host";
  private static final String  KEY_PORT          = "proxy.port";

  ProxyValues(@NonNull KeyValueStore store) {
    super(store);
  }

  @Override
  void onFirstEverAppLaunch() {
  }


  public void enableProxy(@NonNull SignalProxy proxy) {
    getStore().beginWrite()
              .putBoolean(KEY_PROXY_ENABLED, true)
              .putString(KEY_HOST, proxy.getHost())
              .putInteger(KEY_PORT, proxy.getPort())
              .apply();
  }

  /**
   * Disables the proxy, but does not clear out the last-chosen host.
   */
  public void disableProxy() {
    putBoolean(KEY_PROXY_ENABLED, false);
  }

  public boolean isProxyEnabled() {
    return getBoolean(KEY_PROXY_ENABLED, false);
  }

  /**
   * Sets the proxy. This does not *enable* the proxy. This is because the user may want to set a
   * proxy and then enabled it and disable it at will.
   */
  public void setProxy(@Nullable SignalProxy proxy) {
    if (proxy != null) {
      getStore().beginWrite()
                .putString(KEY_HOST, proxy.getHost())
                .putInteger(KEY_PORT, proxy.getPort())
                .apply();
    } else {
      getStore().beginWrite()
                .remove(KEY_HOST)
                .remove(KEY_PORT)
                .apply();
    }
  }

  public @Nullable SignalProxy getProxy() {
    String host = getString(KEY_HOST, null);
    int    port = getInteger(KEY_PORT, 0);

    if (host != null) {
      return new SignalProxy(host, port);
    } else {
      return null;
    }
  }
}
