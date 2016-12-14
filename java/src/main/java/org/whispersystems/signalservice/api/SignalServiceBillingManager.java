package org.whispersystems.signalservice.api;


import com.stripe.model.ProductCollection;

import org.whispersystems.signalservice.api.push.TrustStore;
import org.whispersystems.signalservice.internal.push.BillingInfo;
import org.whispersystems.signalservice.internal.push.PushServiceSocket;
import org.whispersystems.signalservice.internal.util.StaticCredentialsProvider;

import java.io.IOException;

/**
 * The main interface for setting up and managing billing.
 *
 */
public class SignalServiceBillingManager {

    private final PushServiceSocket pushServiceSocket;
    private final String            user;
    private final String            userAgent;

    /**
     * Construct a SignalServiceBillingManager.
     *
     * @param url The URL for the Signal Service.
     * @param trustStore The {@link org.whispersystems.signalservice.api.push.TrustStore} for the SignalService server's TLS certificate.
     * @param user A Signal Service phone number.
     * @param password A Signal Service password.
     * @param userAgent A string which identifies the client software.
     */
    public SignalServiceBillingManager(String url, TrustStore trustStore,
                                       String user, String password,
                                       String userAgent)
    {
        this.pushServiceSocket = new PushServiceSocket(url, trustStore, new StaticCredentialsProvider(user, password, null), userAgent);
        this.user              = user;
        this.userAgent         = userAgent;
    }

    /**
     * Connect the account indicated in the authorization code (retrieved separately) to the billing platform.
     *
     * @param authorizationCode The code provided by the billing service when the account was connected via Stripe.
     * @return A JSON string containing the credentials for use in future billing operations.
     * @throws IOException
     */
    public BillingInfo connectAccount(String authorizationCode) throws IOException {
        return this.pushServiceSocket.connectAccount(authorizationCode);
    }

    /**
     * Revoke the billing credentials to prevent further use.
     *
     * @throws IOException
     */
    public void revokeBillingAccess(String userId) throws IOException {
        this.pushServiceSocket.revokeBillingAccess(userId);
    }

    /**
     * Get the list of products offered by the specified seller.
     * @param sellerNumber The number of the seller to query.
     * @return A list of the products available for purchase.
     * @throws IOException
     */
    public ProductCollection getProducts(String sellerNumber) throws IOException {
        return this.pushServiceSocket.getProducts(sellerNumber);
    }

    /**
     * Get the list of payments (charges) made by the specified contact.
     * @param contactNumber The number of the contact to query.
     * @return A string containing the JSON representation of the charges made by the specified contact.
     * @throws IOException
     */
    public String getPayments(String contactNumber) throws IOException {
        return this.pushServiceSocket.getPayments(contactNumber);
    }

    /**
     * Perform a charge through the billing service proxied through the Signal server for security purposes.
     * @param productId The ID of the product being purchased.
     * @param skuId The ID of the SKU of the product being purchased.
     * @param sourceTokenId The ID of a token representing the card/payment handled separately (through the billing SDK, for example).
     * @param sellerNumber The number of the product seller.
     * @return A string containing the results of the operation (a Charge object in JSON form).
     * @throws IOException
     */
    public String performCharge(String productId, String skuId, String sourceTokenId, String sellerNumber) throws IOException {
        return this.pushServiceSocket.performCharge(productId, skuId, sourceTokenId, sellerNumber);
    }
}
