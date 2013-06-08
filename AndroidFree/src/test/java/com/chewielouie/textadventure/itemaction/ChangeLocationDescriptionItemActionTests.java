package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.ModelLocation;

@RunWith(JMock.class)
public class ChangeLocationDescriptionItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_changes_location_description() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "loc2" );
        final List<ModelLocation> locations = new ArrayList<ModelLocation>();
        locations.add( location1 );
        locations.add( location2 );
        ChangeLocationDescriptionItemAction action = new ChangeLocationDescriptionItemAction( "locid2:new description", model );
        mockery.checking( new Expectations() {{
            allowing( model ).locations();
            will( returnValue( locations ) );
            ignoring( model );
            allowing( location1 ).id();
            will( returnValue( "locid1" ) );
            ignoring( location1 );
            allowing( location2 ).id();
            will( returnValue( "locid2" ) );
            oneOf( location2 ).setLocationDescription( "new description\n" );
            ignoring( location2 );
        }});

        action.enact();
    }
}



