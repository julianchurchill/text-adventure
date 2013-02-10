package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class PlainTextModelPopulatorTests {

    private Mockery mockery = new Mockery();

    @Test
    public void location_is_deserialised_from_location_name_onwards() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            //allowing( locationFactory ).create( with( any( String.class ) ),
                                                //with( any( String.class ) ) );
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( location ).deserialise( "location_name:name" );
            ignoring( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory,
                                     "location_name:name" );
    }

    @Test
    public void multiple_locations_are_deserialised_from_location_name_onwards() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            //allowing( locationFactory ).create( with( any( String.class ) ),
                                                //with( any( String.class ) ) );
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( location ).deserialise( "location_name:name\n" );
            oneOf( location ).deserialise( "location_name:name2\n" );
            ignoring( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory,
                                     "location_name:name\n" +
                                     "location_name:name2\n" );
    }

    @Test
    public void location_is_added_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            //allowing( locationFactory ).create( with( any( String.class ) ),
                                                //with( any( String.class ) ) );
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            ignoring( location );
            oneOf( model ).addLocation( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory,
                                     "location_name:name" );
    }

    @Test
    public void multiple_locations_are_added_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "l1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "l2" );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
          atLeast( 1 ).of( locationFactory ).create();
              will( onConsecutiveCalls(
                      returnValue( location1 ),
                      returnValue( location2 ) ) );
          ignoring( locationFactory );
          ignoring( location1 );
          ignoring( location2 );
          oneOf( model ).addLocation( location1 );
          oneOf( model ).addLocation( location2 );
          ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory,
                                     "location_name:name1\nlocation_name:name2" );
    }

    // Move this to Location.deserialise() tests
    //@Test
    //public void location_descriptions_are_included_on_creation() {
        //final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        //final ModelLocation location1 = mockery.mock( ModelLocation.class, "l1" );
        //final ModelLocationFactory locationFactory =
            //mockery.mock( ModelLocationFactory.class );

        //mockery.checking( new Expectations() {{
            //allowing( locationFactory ).create( "name1",
               //"You are in a room" );
            //will( returnValue( location1 ) );
            //ignoring( locationFactory );
            //ignoring( location1 );
            //oneOf( model ).addLocation( location1 );
            //ignoring( model );
        //}});

        //new PlainTextModelPopulator( model, locationFactory,
                 //"location_name:name1\nlocation description:You are in a room");
    //}

    //@Test
    //public void descriptions_can_include_newlines() {
}

