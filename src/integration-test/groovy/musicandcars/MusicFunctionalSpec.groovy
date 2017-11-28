package musicandcars

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

@Integration
@Stepwise
class MusicFunctionalSpec extends Specification {

    @Shared
    def rest = new RestBuilder()

    void "test that no albums exist"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/albums")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 0
    }

    void "test creating albums"() {
        when:
        def resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName = 'King Crimson'
                title = 'Red'
                genre = 'PROGRESSIVE_ROCK'
            }
        }
        def contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.artistName == 'King Crimson'
        resp.json.title == 'Red'
        resp.json.genre == 'PROGRESSIVE_ROCK'

        when:
        resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName = 'Riverside'
                title = 'Love, Fear and the Time Machine'
                genre = 'PROGRESSIVE_ROCK'
            }
        }
        contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.artistName == 'Riverside'
        resp.json.title == 'Love, Fear and the Time Machine'
        resp.json.genre == 'PROGRESSIVE_ROCK'

        when:
        resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName = 'Johnny Winter'
                title = 'Progressive Blues Experiment'
                genre = 'BLUES'
            }
        }
        contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.artistName == 'Johnny Winter'
        resp.json.title == 'Progressive Blues Experiment'
        resp.json.genre == 'BLUES'


        when:
        resp = rest.post("http://localhost:${serverPort}/albums") {
            json {
                artistName = 'Motorhead'
                title = "No Sleep 'til Hammersmith"
                genre = 'HEAVY_METAL'
            }
        }
        contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.artistName == 'Motorhead'
        resp.json.title == "No Sleep 'til Hammersmith"
        resp.json.genre == 'HEAVY_METAL'
    }


    void 'test retrieving list of albums defaults to JSON'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/albums")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 4

        and:
        resp.json[0].artistName == 'King Crimson'
        resp.json[0].title == 'Red'
        resp.json[0].genre == 'PROGRESSIVE_ROCK'

        and:
        resp.json[1].artistName == 'Riverside'
        resp.json[1].title == 'Love, Fear and the Time Machine'
        resp.json[1].genre == 'PROGRESSIVE_ROCK'

        and:
        resp.json[3].artistName == 'Motorhead'
        resp.json[3].title == "No Sleep 'til Hammersmith"
        resp.json[3].genre == 'HEAVY_METAL'

        and:
        resp.json[2].artistName == 'Johnny Winter'
        resp.json[2].title == 'Progressive Blues Experiment'
        resp.json[2].genre == 'BLUES'
    }
    // tag::genre_query[]
    void "test retrieving albums by genre"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/genre/PROGRESSIVE_ROCK/albums")  // <1>
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 2                                                                // <2>

        when:
        resp = rest.get("http://localhost:${serverPort}/genre/HEAVY_METAL/albums")           // <3>
        contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 1                                                                // <4>

        when:
        resp = rest.get("http://localhost:${serverPort}/genre/BLUES/albums")                 // <5>
        contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 1                                                                // <6>
    }
    // end::genre_query[]
}
