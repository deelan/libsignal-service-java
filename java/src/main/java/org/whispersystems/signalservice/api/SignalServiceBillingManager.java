package org.whispersystems.signalservice.api;


import com.stripe.model.PlanCollection;
import com.stripe.model.ProductCollection;

import org.whispersystems.signalservice.internal.push.BillingInfo;
import org.whispersystems.signalservice.internal.push.PushServiceSocket;
import org.whispersystems.signalservice.internal.push.SignalServiceUrl;
import org.whispersystems.signalservice.internal.util.StaticCredentialsProvider;

import java.io.IOException;
import java.util.Map;

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
     * @param urls The URL for the Signal Service.
     * @param user A Signal Service phone number.
     * @param password A Signal Service password.
     * @param userAgent A string which identifies the client software.
     */
    public SignalServiceBillingManager(SignalServiceUrl[] urls,
                                       String user, String password,
                                       String userAgent)
    {
        this.pushServiceSocket = new PushServiceSocket(urls, new StaticCredentialsProvider(user, password, null), userAgent);
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
     * Return the list of plans offered by the specified seller.
     * @param sellerNumber The phone number of the seller to query.
     * @return A list of the plans available for subscription.
     * @throws IOException
     */
    public PlanCollection getPlans(String sellerNumber) throws IOException {
        return this.pushServiceSocket.getPlans(sellerNumber);
    }

    /**
     * Get the list of products offered by the specified seller.
     * @param sellerNumber The phone number of the seller to query.
     * @return A list of the products available for purchase.
     * @throws IOException
     */
    public ProductCollection getProducts(String sellerNumber) throws IOException {
        return this.pushServiceSocket.getProducts(sellerNumber);
    }

    /**
     * Get the list of payments (charges) made by the specified contact.
     * @param contactNumber The phone number of the contact to query.
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
     * @param sellerNumber The phone number of the product seller.
     * @param productName The name of the product.
     * @return A string containing the results of the operation (a Charge object in JSON form).
     * @throws IOException
     */
    public String performCharge(String productId, String skuId, String sourceTokenId, String sellerNumber, String productName) throws IOException {
        return this.pushServiceSocket.performCharge(productId, skuId, sourceTokenId, sellerNumber, productName);
    }

    /**
     * Get the Stripe customer IDs on file for the current user.
     * @return A map containing all the Stripe customer IDs in name-value pair format.
     * @throws IOException
     */
    public Map<String, String> getCustomerIds() throws IOException {
        return this.pushServiceSocket.getCustomerIds();
    }

    /**
     * Purchase a subscription to the specified plan using the specified payment token, or the stored payment details, if no payment token is provided.
     * @param planId The ID of the plan to which the user is subscribing.
     * @param sourceTokenId The card token, if needed.
     * @param sellerNumber The phone number of the seller in the transaction.
     * @param planName The display name of the plan.
     * @return A string containing the results of the operation.
     * @throws IOException
     */
    public String subscribeToPlan(String planId, String sourceTokenId, String sellerNumber, String planName) throws IOException {
        return this.pushServiceSocket.subscribeToPlan(planId, sourceTokenId, sellerNumber, planName);
    }
}
