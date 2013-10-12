// package REPLACE_ME;

// Help can be found here https://github.com/cucumber/cucumber-jvm

// import cucumber.annotation.After;
// import cucumber.annotation.Before;
// import cucumber.api.java.en.Given;
// import cucumber.api.java.en.Then;
// import cucumber.api.java.en.When;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.*;

// public class CucumberStepDefs {
//     private Model model;
//     private UserInventory inventory;

//     @Before(@reload_TTA2)
//     public void reload_TTA2_model() {
//         initialiseModel( loadFile( "config/2/res/raw/model_content.txt" ) );
//     }

//     private void initialiseModel( String model_content ) {
//         model = (BasicModel)modelFactory().createModel();
//         inventory = model;
//         ItemActionFactory itemActionFactory = new LoggableNormalItemActionFactory( logger, model );
//         ItemFactory itemFactory = new NormalItemFactory();
//         ItemDeserialiser itemDeserialiser =
//             new PlainTextItemDeserialiser( itemActionFactory );
//         new PlainTextModelPopulator( model,
//             new LocationFactory( inventory, actionFactory() ),
//             inventory,
//             itemFactory,
//             new PlainTextModelLocationDeserialiser(
//                 itemFactory, new LocationExitFactory(),
//                 itemDeserialiser,
//                 new PlainTextExitDeserialiser( itemActionFactory ) ),
//             itemDeserialiser,
//             model_content );
//     }

//     private String loadFile( String filename ) {
//         return "";
//     }

//     @Given("^The player has a \"([^\"]*)\" and a \"([^\"]*)\"$")
//     public void given_the_player_has_a_xxx_and_a_xxx(String itemID1, String itemID2) {
//         the_player_has_a( itemID1 );
//         the_player_has_a( itemID2 );
//     }

//     @Given("^The player has a \"([^\"]*)\"$")
//     public void given_the_player_has_a_xxx(String itemID) {
//         TakeItemItemAction a = new TakeItemItemAction( itemID, model );
//         a.enact();
//     }

//     @When("^They use the \"([^\"]*)\" with the \"([^\"]*)\"$")
//     public void they_use_the_xxx_with_the_xxx(String itemID1, String itemID2) {
//         UseWithSpecificItem a = new UseWithSpecificItem(
//             model.findItemByID( itemID1 ), model.findItemByID( itemID2 ) );
//         a.trigger();
//     }

//     @Then("^The player has a \"([^\"]*)\"$")
//     public void then_the_player_has_a_xxx(String itemID) {
//         assertThat( inventory.inventoryItems(), hasItem( itemID ) );
//     }

//     @Then("^The player does not have a \"([^\"]*)\"$")
//     public void then_the_player_does_not_have_a_xxx(String itemID) {
//         assertThat( inventory.inventoryItems(), not( hasItem( itemID ) ) );
//     }
// }
