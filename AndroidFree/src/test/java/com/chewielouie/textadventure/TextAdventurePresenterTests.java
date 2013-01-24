package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TextAdventurePresenterTests {

    private Mockery mockery = new Mockery();

    @Test
    public void failing_test() {
        fail("Fail");
    }

    //@Test
    //public void should_tell_view_about_board_when_rendering() {
        //final PlayGameView view = mockery.mock( PlayGameView.class );
        //final Board board = mockery.mock( Board.class );
        //mockery.checking( new Expectations() {{
            //oneOf( view ).displayBoard( board );
            //ignoring( board );
        //}});
        //PlayGamePresenter p = new PlayGamePresenter( board, view );

        //p.render();
    //}
}

