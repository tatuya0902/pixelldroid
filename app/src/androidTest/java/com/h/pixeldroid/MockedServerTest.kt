package com.h.pixeldroid

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MockedServerTest {
    private val accountJson = "{\n" +
            "      \"id\": \"1450\",\n" +
            "      \"username\": \"deerbard_photo\",\n" +
            "      \"acct\": \"deerbard_photo\",\n" +
            "      \"display_name\": \"deerbard photography\",\n" +
            "      \"locked\": false,\n" +
            "      \"created_at\": \"2018-08-01T12:58:21.000000Z\",\n" +
            "      \"followers_count\": 68,\n" +
            "      \"following_count\": 27,\n" +
            "      \"statuses_count\": 72,\n" +
            "      \"note\": \"\",\n" +
            "      \"url\": \"https://pixelfed.social/deerbard_photo\",\n" +
            "      \"avatar\": \"https://pixelfed.social/storage/avatars/000/000/001/450/SMSep5NoabDam1W8UDMh_avatar.png?v=4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a\",\n" +
            "      \"avatar_static\": \"https://pixelfed.social/storage/avatars/000/000/001/450/SMSep5NoabDam1W8UDMh_avatar.png?v=4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a\",\n" +
            "      \"header\": \"\",\n" +
            "      \"header_static\": \"\",\n" +
            "      \"emojis\": [],\n" +
            "      \"moved\": null,\n" +
            "      \"fields\": null,\n" +
            "      \"bot\": false,\n" +
            "      \"software\": \"pixelfed\",\n" +
            "      \"is_admin\": false\n" +
            "    }"
    private val feedJson = """[{"id":"140364967936397312","uri":"https:\/\/pixelfed.de\/p\/Miike\/140364967936397312","url":"https:\/\/pixelfed.de\/p\/Miike\/140364967936397312","in_reply_to_id":null,"in_reply_to_account_id":null,"reblog":null,"content":"Day 8 <a href=\"https:\/\/pixelfed.de\/discover\/tags\/rotavicentina?src=hash\" title=\"#rotavicentina\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#rotavicentina<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/hiking?src=hash\" title=\"#hiking\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#hiking<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/nature?src=hash\" title=\"#nature\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#nature<\/a>","created_at":"2020-03-03T08:00:16.000000Z","emojis":[],"replies_count":0,"reblogs_count":0,"favourites_count":0,"reblogged":null,"favourited":null,"muted":null,"sensitive":false,"spoiler_text":"","visibility":"public","mentions":[],"tags":[{"name":"hiking","url":"https:\/\/pixelfed.de\/discover\/tags\/hiking"},{"name":"nature","url":"https:\/\/pixelfed.de\/discover\/tags\/nature"},{"name":"rotavicentina","url":"https:\/\/pixelfed.de\/discover\/tags\/rotavicentina"}],"card":null,"poll":null,"application":{"name":"web","website":null},"language":null,"pinned":null,"account":{"id":"115114166443970560","username":"Miike","acct":"Miike","display_name":"Miike Duart","locked":false,"created_at":"2019-12-24T15:42:35.000000Z","followers_count":14,"following_count":0,"statuses_count":71,"note":"","url":"https:\/\/pixelfed.de\/Miike","avatar":"https:\/\/pixelfed.de\/storage\/avatars\/011\/511\/416\/644\/397\/056\/0\/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","avatar_static":"https:\/\/pixelfed.de\/storage\/avatars\/011\/511\/416\/644\/397\/056\/0\/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","header":"","header_static":"","emojis":[],"moved":null,"fields":null,"bot":false,"software":"pixelfed","is_admin":false},"media_attachments":[{"id":"15888","type":"image","url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f\/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB.jpeg","remote_url":null,"preview_url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f\/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB_thumb.jpeg","text_url":null,"meta":null,"description":null}]},{"id":"140349785193451520","uri":"https:\/\/pixelfed.de\/p\/stephan\/140349785193451520","url":"https:\/\/pixelfed.de\/p\/stephan\/140349785193451520","in_reply_to_id":null,"in_reply_to_account_id":null,"reblog":null,"content":"","created_at":"2020-03-03T06:59:56.000000Z","emojis":[],"replies_count":0,"reblogs_count":0,"favourites_count":2,"reblogged":null,"favourited":null,"muted":null,"sensitive":false,"spoiler_text":"","visibility":"public","mentions":[],"tags":[],"card":null,"poll":null,"application":{"name":"web","website":null},"language":null,"pinned":null,"account":{"id":"908","username":"stephan","acct":"stephan","display_name":"Stephan","locked":false,"created_at":"2019-03-17T07:46:33.000000Z","followers_count":136,"following_count":25,"statuses_count":136,"note":"Musician, software developer &amp; hobby photographer.","url":"https:\/\/pixelfed.de\/stephan","avatar":"https:\/\/pixelfed.de\/storage\/avatars\/000\/000\/000\/908\/5nQzzsB1mkwKaUqQ9GNN_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","avatar_static":"https:\/\/pixelfed.de\/storage\/avatars\/000\/000\/000\/908\/5nQzzsB1mkwKaUqQ9GNN_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","header":"","header_static":"","emojis":[],"moved":null,"fields":null,"bot":false,"software":"pixelfed","is_admin":false},"media_attachments":[{"id":"15887","type":"image","url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/a1349f5183c2bac7d52880e8f5188df0f3b2d62a\/mvT3nYV6Wdu42Xh56Ny4VYaWU0OzbnC3wjxiqnKS.jpeg","remote_url":null,"preview_url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/a1349f5183c2bac7d52880e8f5188df0f3b2d62a\/mvT3nYV6Wdu42Xh56Ny4VYaWU0OzbnC3wjxiqnKS_thumb.jpeg","text_url":null,"meta":null,"description":null}]},{"id":"140276879742603264","uri":"https:\/\/pixelfed.de\/p\/fegrimaldi\/140276879742603264","url":"https:\/\/pixelfed.de\/p\/fegrimaldi\/140276879742603264","in_reply_to_id":null,"in_reply_to_account_id":null,"reblog":null,"content":"february 2 is the day to give flowers to Iemanj\u00e1. <a href=\"https:\/\/pixelfed.de\/discover\/tags\/salvador?src=hash\" title=\"#salvador\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#salvador<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/bahia?src=hash\" title=\"#bahia\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#bahia<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/brazil?src=hash\" title=\"#brazil\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#brazil<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/iemanja?src=hash\" title=\"#iemanja\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#iemanja<\/a>","created_at":"2020-03-03T02:10:14.000000Z","emojis":[],"replies_count":0,"reblogs_count":0,"favourites_count":1,"reblogged":null,"favourited":null,"muted":null,"sensitive":false,"spoiler_text":"","visibility":"public","mentions":[],"tags":[{"name":"salvador","url":"https:\/\/pixelfed.de\/discover\/tags\/salvador"},{"name":"bahia","url":"https:\/\/pixelfed.de\/discover\/tags\/bahia"},{"name":"brazil","url":"https:\/\/pixelfed.de\/discover\/tags\/brazil"},{"name":"iemanja","url":"https:\/\/pixelfed.de\/discover\/tags\/iemanja"}],"card":null,"poll":null,"application":{"name":"web","website":null},"language":null,"pinned":null,"account":{"id":"137257212828585984","username":"fegrimaldi","acct":"fegrimaldi","display_name":"Fernanda Grimaldi","locked":false,"created_at":"2020-02-23T18:11:09.000000Z","followers_count":2,"following_count":7,"statuses_count":2,"note":"a little piece of Bahia in the fediverse.","url":"https:\/\/pixelfed.de\/fegrimaldi","avatar":"https:\/\/pixelfed.de\/storage\/avatars\/013\/725\/721\/282\/858\/598\/4\/oUPBit0TJso1xNhJfFqg_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","avatar_static":"https:\/\/pixelfed.de\/storage\/avatars\/013\/725\/721\/282\/858\/598\/4\/oUPBit0TJso1xNhJfFqg_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","header":"","header_static":"","emojis":[],"moved":null,"fields":null,"bot":false,"software":"pixelfed","is_admin":false},"media_attachments":[{"id":"15886","type":"image","url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/feb878b4bd60b85ac840670c6b9c809fd76b628b\/lYMrx0WF8LDqn0vTRgNJaRs7stMKtAXrgzpMrWEr.jpeg","remote_url":null,"preview_url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/feb878b4bd60b85ac840670c6b9c809fd76b628b\/lYMrx0WF8LDqn0vTRgNJaRs7stMKtAXrgzpMrWEr_thumb.jpeg","text_url":null,"meta":null,"description":null}]}]"""
    private val notificationsJson = "[{\"id\":\"45945\",\"type\":\"favourite\",\"created_at\":\"2020-03-15T14:49:20.000000Z\",\"account\":{\"id\":\"136800034732773376\",\"username\":\"Dobios\",\"acct\":\"Dobios\",\"display_name\":\"Andrew Dobis\",\"locked\":false,\"created_at\":\"2020-02-22T11:54:29.000000Z\",\"followers_count\":2,\"following_count\":1,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Dobios\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144456497894658048\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":null,\"reblog\":null,\"content\":\"Saturn V launch\",\"created_at\":\"2020-03-14T14:58:32.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":1,\"favourites_count\":6,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"136453537340198912\",\"username\":\"Geonosys\",\"acct\":\"dante\",\"display_name\":\"Dante\",\"locked\":false,\"created_at\":\"2020-02-21T12:57:38.000000Z\",\"followers_count\":3,\"following_count\":4,\"statuses_count\":1,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"media_attachments\":[{\"id\":\"16583\",\"type\":\"image\",\"url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad.jpeg\",\"remote_url\":null,\"preview_url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad_thumb.jpeg\",\"text_url\":null,\"meta\":null,\"description\":null}]}},{\"id\":\"45944\",\"type\":\"follow\",\"created_at\":\"2020-03-15T14:49:11.000000Z\",\"account\":{\"id\":\"136800034732773376\",\"username\":\"Dobios\",\"acct\":\"Dobios\",\"display_name\":\"Andrew Dobis\",\"locked\":false,\"created_at\":\"2020-02-22T11:54:29.000000Z\",\"followers_count\":2,\"following_count\":1,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Dobios\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false}},{\"id\":\"45942\",\"type\":\"reblog\",\"created_at\":\"2020-03-15T14:41:04.000000Z\",\"account\":{\"id\":\"144813993922531328\",\"username\":\"Clement\",\"acct\":\"Clement\",\"display_name\":\"Andrea\",\"locked\":false,\"created_at\":\"2020-03-15T14:39:06.000000Z\",\"followers_count\":0,\"following_count\":2,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Clement\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144814478708576256\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/Clement\\/144814478708576256\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/Clement\\/144814478708576256\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":136453537340198912,\"reblog\":null,\"content\":\"\",\"created_at\":\"2020-03-15T14:41:02.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":0,\"favourites_count\":0,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"144813993922531328\",\"username\":\"Clement\",\"acct\":\"Clement\",\"display_name\":\"Andrea\",\"locked\":false,\"created_at\":\"2020-03-15T14:39:06.000000Z\",\"followers_count\":0,\"following_count\":2,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Clement\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false}}},{\"id\":\"45941\",\"type\":\"mention\",\"created_at\":\"2020-03-15T14:40:52.000000Z\",\"account\":{\"id\":\"144813993922531328\",\"username\":\"Clement\",\"acct\":\"Clement\",\"display_name\":\"Andrea\",\"locked\":false,\"created_at\":\"2020-03-15T14:39:06.000000Z\",\"followers_count\":0,\"following_count\":2,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Clement\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144814428691501056\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/Clement\\/144814428691501056\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/Clement\\/144814428691501056\",\"in_reply_to_id\":144456497894658048,\"in_reply_to_account_id\":136453537340198912,\"reblog\":null,\"content\":\"<a class=\\\"u-url mention\\\" href=\\\"https:\\/\\/pixelfed.de\\/dante\\\" rel=\\\"external nofollow noopener\\\" target=\\\"_blank\\\">@dante<\\/a> I identify to this pic.\",\"created_at\":\"2020-03-15T14:40:50.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":0,\"favourites_count\":1,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[{\"id\":\"136453537340198912\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"username\":\"dante\",\"acct\":\"dante\"}],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"144813993922531328\",\"username\":\"Clement\",\"acct\":\"Clement\",\"display_name\":\"Andrea\",\"locked\":false,\"created_at\":\"2020-03-15T14:39:06.000000Z\",\"followers_count\":0,\"following_count\":2,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Clement\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false}}},{\"id\":\"45940\",\"type\":\"favourite\",\"created_at\":\"2020-03-15T14:40:22.000000Z\",\"account\":{\"id\":\"144813993922531328\",\"username\":\"Clement\",\"acct\":\"Clement\",\"display_name\":\"Andrea\",\"locked\":false,\"created_at\":\"2020-03-15T14:39:06.000000Z\",\"followers_count\":0,\"following_count\":2,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Clement\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144456497894658048\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":null,\"reblog\":null,\"content\":\"Saturn V launch\",\"created_at\":\"2020-03-14T14:58:32.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":1,\"favourites_count\":6,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"136453537340198912\",\"username\":\"dante\",\"acct\":\"dante\",\"display_name\":\"Dante\",\"locked\":false,\"created_at\":\"2020-02-21T12:57:38.000000Z\",\"followers_count\":3,\"following_count\":4,\"statuses_count\":1,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"media_attachments\":[{\"id\":\"16583\",\"type\":\"image\",\"url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad.jpeg\",\"remote_url\":null,\"preview_url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad_thumb.jpeg\",\"text_url\":null,\"meta\":null,\"description\":null}]}},{\"id\":\"45939\",\"type\":\"follow\",\"created_at\":\"2020-03-15T14:40:12.000000Z\",\"account\":{\"id\":\"144813993922531328\",\"username\":\"Clement\",\"acct\":\"Clement\",\"display_name\":\"Andrea\",\"locked\":false,\"created_at\":\"2020-03-15T14:39:06.000000Z\",\"followers_count\":0,\"following_count\":2,\"statuses_count\":0,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Clement\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false}},{\"id\":\"45804\",\"type\":\"favourite\",\"created_at\":\"2020-03-15T02:22:47.000000Z\",\"account\":{\"id\":\"131984031779786752\",\"username\":\"joska\",\"acct\":\"joska\",\"display_name\":\"jxzk\",\"locked\":false,\"created_at\":\"2020-02-09T04:57:25.000000Z\",\"followers_count\":3,\"following_count\":2,\"statuses_count\":82,\"note\":\"Feliz :D\",\"url\":\"https:\\/\\/pixelfed.de\\/joska\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/013\\/198\\/403\\/177\\/978\\/675\\/2\\/zhytNrT3ij5cHBXX1mJv_avatar.jpeg?v=4fc82b26aecb47d2868c4efbe3581732a3e7cbcc6c2efb32062c08170a05eeb8\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/013\\/198\\/403\\/177\\/978\\/675\\/2\\/zhytNrT3ij5cHBXX1mJv_avatar.jpeg?v=4fc82b26aecb47d2868c4efbe3581732a3e7cbcc6c2efb32062c08170a05eeb8\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144456497894658048\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":null,\"reblog\":null,\"content\":\"Saturn V launch\",\"created_at\":\"2020-03-14T14:58:32.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":1,\"favourites_count\":6,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"136453537340198912\",\"username\":\"dante\",\"acct\":\"dante\",\"display_name\":\"Dante\",\"locked\":false,\"created_at\":\"2020-02-21T12:57:38.000000Z\",\"followers_count\":3,\"following_count\":4,\"statuses_count\":1,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"media_attachments\":[{\"id\":\"16583\",\"type\":\"image\",\"url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad.jpeg\",\"remote_url\":null,\"preview_url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad_thumb.jpeg\",\"text_url\":null,\"meta\":null,\"description\":null}]}},{\"id\":\"45783\",\"type\":\"favourite\",\"created_at\":\"2020-03-15T00:45:53.000000Z\",\"account\":{\"id\":\"139939422090170368\",\"username\":\"DrMsch\",\"acct\":\"DrMsch\",\"display_name\":\"Mische\",\"locked\":false,\"created_at\":\"2020-03-02T03:49:18.000000Z\",\"followers_count\":13,\"following_count\":21,\"statuses_count\":9,\"note\":\"TelefonFotos und Malereien von DerMische\",\"url\":\"https:\\/\\/pixelfed.de\\/DrMsch\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/013\\/993\\/942\\/209\\/017\\/036\\/8\\/xQUOq3tBNgOhFItKMZ56_avatar.jpeg?v=4fc82b26aecb47d2868c4efbe3581732a3e7cbcc6c2efb32062c08170a05eeb8\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/013\\/993\\/942\\/209\\/017\\/036\\/8\\/xQUOq3tBNgOhFItKMZ56_avatar.jpeg?v=4fc82b26aecb47d2868c4efbe3581732a3e7cbcc6c2efb32062c08170a05eeb8\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144456497894658048\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":null,\"reblog\":null,\"content\":\"Saturn V launch\",\"created_at\":\"2020-03-14T14:58:32.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":1,\"favourites_count\":6,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"136453537340198912\",\"username\":\"dante\",\"acct\":\"dante\",\"display_name\":\"Dante\",\"locked\":false,\"created_at\":\"2020-02-21T12:57:38.000000Z\",\"followers_count\":3,\"following_count\":4,\"statuses_count\":1,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"media_attachments\":[{\"id\":\"16583\",\"type\":\"image\",\"url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad.jpeg\",\"remote_url\":null,\"preview_url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad_thumb.jpeg\",\"text_url\":null,\"meta\":null,\"description\":null}]}},{\"id\":\"45768\",\"type\":\"favourite\",\"created_at\":\"2020-03-14T22:43:18.000000Z\",\"account\":{\"id\":\"139819612522024960\",\"username\":\"vitorpires\",\"acct\":\"vitorpires\",\"display_name\":\"Vitor Pires\",\"locked\":false,\"created_at\":\"2020-03-01T19:53:13.000000Z\",\"followers_count\":20,\"following_count\":8,\"statuses_count\":42,\"note\":\"photography\\/graphic design\\/desktop publishing\\/illustration\\/3D animation\\/video\\/content writing\\/teaching\\/woodworking\\/luthier\",\"url\":\"https:\\/\\/pixelfed.de\\/vitorpires\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/013\\/981\\/961\\/252\\/202\\/496\\/0\\/2HB6Gs2m5NaSys7W5ikG_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/013\\/981\\/961\\/252\\/202\\/496\\/0\\/2HB6Gs2m5NaSys7W5ikG_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144456497894658048\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":null,\"reblog\":null,\"content\":\"Saturn V launch\",\"created_at\":\"2020-03-14T14:58:32.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":1,\"favourites_count\":6,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"136453537340198912\",\"username\":\"dante\",\"acct\":\"dante\",\"display_name\":\"Dante\",\"locked\":false,\"created_at\":\"2020-02-21T12:57:38.000000Z\",\"followers_count\":3,\"following_count\":4,\"statuses_count\":1,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"media_attachments\":[{\"id\":\"16583\",\"type\":\"image\",\"url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad.jpeg\",\"remote_url\":null,\"preview_url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad_thumb.jpeg\",\"text_url\":null,\"meta\":null,\"description\":null}]}},{\"id\":\"45723\",\"type\":\"favourite\",\"created_at\":\"2020-03-14T15:01:49.000000Z\",\"account\":{\"id\":\"79574199701737472\",\"username\":\"Spaziergaenger\",\"acct\":\"Spaziergaenger\",\"display_name\":\"anonymous\",\"locked\":false,\"created_at\":\"2019-09-17T13:59:27.000000Z\",\"followers_count\":40,\"following_count\":0,\"statuses_count\":894,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/Spaziergaenger\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/007\\/957\\/419\\/970\\/173\\/747\\/2\\/KEg4YgCgsmzdgyVztszz_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/007\\/957\\/419\\/970\\/173\\/747\\/2\\/KEg4YgCgsmzdgyVztszz_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"status\":{\"id\":\"144456497894658048\",\"uri\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"url\":\"https:\\/\\/pixelfed.de\\/p\\/dante\\/144456497894658048\",\"in_reply_to_id\":null,\"in_reply_to_account_id\":null,\"reblog\":null,\"content\":\"Saturn V launch\",\"created_at\":\"2020-03-14T14:58:32.000000Z\",\"emojis\":[],\"replies_count\":0,\"reblogs_count\":1,\"favourites_count\":6,\"reblogged\":null,\"favourited\":null,\"muted\":null,\"sensitive\":false,\"spoiler_text\":\"\",\"visibility\":\"public\",\"mentions\":[],\"tags\":[],\"card\":null,\"poll\":null,\"application\":{\"name\":\"web\",\"website\":null},\"language\":null,\"pinned\":null,\"account\":{\"id\":\"136453537340198912\",\"username\":\"dante\",\"acct\":\"dante\",\"display_name\":\"Dante\",\"locked\":false,\"created_at\":\"2020-02-21T12:57:38.000000Z\",\"followers_count\":3,\"following_count\":4,\"statuses_count\":1,\"note\":\"\",\"url\":\"https:\\/\\/pixelfed.de\\/dante\",\"avatar\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"avatar_static\":\"https:\\/\\/pixelfed.de\\/storage\\/avatars\\/default.png?v=5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9\",\"header\":\"\",\"header_static\":\"\",\"emojis\":[],\"moved\":null,\"fields\":null,\"bot\":false,\"software\":\"pixelfed\",\"is_admin\":false},\"media_attachments\":[{\"id\":\"16583\",\"type\":\"image\",\"url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad.jpeg\",\"remote_url\":null,\"preview_url\":\"https:\\/\\/pixelfed.de\\/storage\\/m\\/113a3e2124a33b1f5511e531953f5ee48456e0c7\\/0fa8bbe19cc23442034913a7c97fbe4527c1d63a\\/vs2vouJ86OvzxhK9ewhPlfPf4Y9IoQ5CHfiBIqad_thumb.jpeg\",\"text_url\":null,\"meta\":null,\"description\":null}]}}]"

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)
    private val dispatcher: Dispatcher = object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            when (request.path) {
                "/api/v1/accounts/verify_credentials" -> return MockResponse().addHeader("Content-Type", "application/json; charset=utf-8").setResponseCode(200).setBody(accountJson)
                "/api/v1/timelines/home" -> return MockResponse().addHeader("Content-Type", "application/json; charset=utf-8").setResponseCode(200).setBody(feedJson)
            }
            if(request.path?.startsWith("/api/v1/notifications") == true) {
                return MockResponse()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setResponseCode(200).setBody(notificationsJson)
            }
            return MockResponse().setResponseCode(404)
        }
    }

    @Before
    fun before(){
        val server = MockWebServer()
        server.dispatcher = dispatcher
        server.start()
        val baseUrl = server.url("")
        val preferences = InstrumentationRegistry.getInstrumentation()
            .targetContext.getSharedPreferences("com.h.pixeldroid.pref", Context.MODE_PRIVATE)
        preferences.edit().putString("accessToken", "azerty").apply()
        preferences.edit().putString("domain", baseUrl.toString()).apply()
        ActivityScenario.launch(MainActivity::class.java)
    }
    @Test
    fun testFollowersTextView() {
        onView(withId(R.id.view_pager)).perform(ViewActions.swipeLeft()).perform(ViewActions.swipeLeft()).perform(
            ViewActions.swipeLeft()
        ).perform(ViewActions.swipeLeft())
        Thread.sleep(1000)
        onView(withId(R.id.nbFollowersTextView)).check(matches(withText("68\nFollowers")))
        onView(withId(R.id.accountNameTextView)).check(matches(withText("deerbard_photo")))
    }

    @Test
    fun swipingDownOnHomepageShowsMorePosts() {
        Thread.sleep(1000)

        val firstDesc = withId(R.id.description)
        onView(withId(R.id.view_pager)).perform(ViewActions.swipeUp()).perform(ViewActions.swipeDown()).perform(
            ViewActions.swipeDown()
        )
        onView(withId(R.id.description)).check(matches(firstDesc))
    }
    @Test
    fun testNotificationsList() {
        onView(withId(R.id.view_pager)).perform(ViewActions.swipeLeft()).perform(
            ViewActions.swipeLeft()
        ).perform(ViewActions.swipeLeft())
        Thread.sleep(1000)

        onView(withId(R.id.view_pager)).perform(ViewActions.swipeUp()).perform(ViewActions.swipeDown())
        onView(withText("Dobios liked your post")).check(matches(withId(R.id.notification_type)))
        onView(withId(R.id.view_pager)).perform(ViewActions.swipeDown())
        Thread.sleep(1000)
        onView(withText("Dobios followed you")).check(matches(withId(R.id.notification_type)))

    }
    @Test
    fun clickNotification() {
        onView(withId(R.id.view_pager)).perform(ViewActions.swipeLeft()).perform(
            ViewActions.swipeLeft()
        ).perform(ViewActions.swipeLeft())
        Thread.sleep(1000)

        onView(withId(R.id.view_pager)).perform(ViewActions.swipeUp()).perform(ViewActions.swipeDown())
        Thread.sleep(1000)

        onView(withText("Dobios liked your post")).perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withText("Geonosys")).check(matches(withId(R.id.username)))
    }

}