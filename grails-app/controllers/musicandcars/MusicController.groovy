package musicandcars

import grails.rest.RestfulController

class MusicController extends RestfulController<Album> {

    static responseFormats = ['json', 'xml']

    public MusicController(){
        super(Album)
    }

    @Override
    protected List<Album> listAllResources(Map params) {
        // return a List of Album here

        // if params.genre exists, only return
        // albums that are of that genre

        // there are several query techniques
        // which could be used here.  Use a
        // "where" query.

        // Option 1
//        if(params.genre) {
//            Album.where{
//                genre == params.genre
//            }
//        }
//        else {
//            Album.list()
//        }
//
//        // Option 2
//        Album.where {
//            if(params.genre) {
//                genre == params.genre
//            }
//        }

        // Option 3 (the best)
        Album.findAll{
            if(params.genre) {
                genre == params.genre
            }
        }



    }

}
