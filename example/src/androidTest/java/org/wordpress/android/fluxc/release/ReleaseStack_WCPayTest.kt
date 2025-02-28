package org.wordpress.android.fluxc.release

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.wordpress.android.fluxc.example.test.BuildConfig
import org.wordpress.android.fluxc.model.pay.WCPaymentAccountResult.WCPayAccountStatusEnum
import org.wordpress.android.fluxc.store.AccountStore.AuthenticatePayload
import org.wordpress.android.fluxc.store.WCPayStore
import javax.inject.Inject

class ReleaseStack_WCPayTest : ReleaseStack_WCBase() {
    @Inject internal lateinit var payStore: WCPayStore

    override fun buildAuthenticatePayload() = AuthenticatePayload(
            BuildConfig.TEST_WPCOM_USERNAME_WOO_JP_WCPAY,
            BuildConfig.TEST_WPCOM_PASSWORD_WOO_JP_WCPAY
    )

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        mReleaseStackAppComponent.inject(this)
        // Register
        init()
    }

    @Test
    fun givenSiteHasWCPayWhenFetchConnectionTokenInvokedThenTokenReturned() = runBlocking {
        val result = payStore.fetchConnectionToken(sSite)

        assertTrue(result.model?.token?.isNotEmpty() == true)
    }

    @Test
    fun givenSiteHasWCPayWhenLoadAccountThenTestAccountReturned() = runBlocking {
        val result = payStore.loadAccount(sSite)

        assertEquals(result.model?.country, "US")
        assertEquals(result.model?.hasPendingRequirements, false)
        assertEquals(result.model?.hasOverdueRequirements, false)
        assertEquals(result.model?.statementDescriptor, "DO.WPMT.CO")
        assertEquals(result.model?.country, "US")
        assertEquals(result.model?.isCardPresentEligible, false)
        assertEquals(result.model?.storeCurrencies?.default, "usd")
        assertEquals(result.model?.storeCurrencies?.supportedCurrencies, listOf("usd"))
        assertEquals(result.model?.status, WCPayAccountStatusEnum.COMPLETE)
    }
}
