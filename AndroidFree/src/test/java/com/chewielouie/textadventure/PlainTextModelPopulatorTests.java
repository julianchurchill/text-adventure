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
    public void location_is_added_by_name_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create( "name", "" );
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
    public void multiple_locations_are_added_by_name_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "l1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "l2" );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create( "name1", "" );
            will( returnValue( location1 ) );
            allowing( locationFactory ).create( "name2", "" );
            will( returnValue( location2 ) );
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

    @Test
    public void location_descriptions_are_included_on_creation() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "l1" );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create( "name1",
               "You are in a room" );
            will( returnValue( location1 ) );
            ignoring( locationFactory );
            ignoring( location1 );
            oneOf( model ).addLocation( location1 );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory,
                 "location_name:name1\nlocation description:You are in a room");
    }

    //@Test
    //public void descriptions_can_include_newlines() {
}

