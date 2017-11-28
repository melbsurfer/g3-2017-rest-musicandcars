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
class CarFunctionalSpec extends Specification {

    @Shared
    def rest = new RestBuilder()

    void "test that no cars exist"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/automobiles")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 0
    }

    void "test creating a car"() {
        when:
        def resp = rest.post("http://localhost:${serverPort}/automobiles") {
            json {
                make = 'Chevy'
                model = 'Equinox'
            }
        }
        def contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.make == 'Chevy'
        resp.json.model == 'Equinox'

        when:
        resp = rest.post("http://localhost:${serverPort}/automobiles") {
            json {
                make = 'Ford'
                model = 'Fusion'
            }
        }
        contentType = resp.headers.getContentType()

        then:
        resp.status == CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.make == 'Ford'
        resp.json.model == 'Fusion'
    }

    void 'test retrieving list of cars defaults to JSON'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/automobiles")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 2

        and:
        resp.json[0].make == 'Chevy'
        resp.json[0].model == 'Equinox'

        and:
        resp.json[1].make == 'Ford'
        resp.json[1].model == 'Fusion'
    }

    void 'test retrieving list of cars as JSON'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/automobiles.json")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 2

        and:
        resp.json[0].make == 'Chevy'
        resp.json[0].model == 'Equinox'

        and:
        resp.json[1].make == 'Ford'
        resp.json[1].model == 'Fusion'
    }

    void 'test retrieving list of cars as XML'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/automobiles.xml")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == OK.value()
        contentType.subtype == 'xml'
        contentType.type == 'text'
        resp.xml.car.size() == 2

        and:
        resp.xml.car[0].make.text() == 'Chevy'
        resp.xml.car[0].model.text() == 'Equinox'

        and:
        resp.xml.car[1].make.text() == 'Ford'
        resp.xml.car[1].model.text() == 'Fusion'
    }
}
