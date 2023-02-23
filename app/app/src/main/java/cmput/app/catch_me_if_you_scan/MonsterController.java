package cmput.app.catch_me_if_you_scan;

public class MonsterController {

    /*
    Takes in a Monster object and sends it to the database.
    Validates Monster fields to ensure all necessary fields are filled in
    and returns "true" if the creation was successful and false if it was
    unsuccessful along with a console log of the issue.
     */
    /**
     * Adds Monster to Database Collection.
     * @return A boolean value correlated with the success of the database write.
     */
    public boolean create() {
        //TODO
        return false;
    }

    //TODO
    /*public Monster read()*/

    /*
    Takes in a Monster object that has its values changed.
    The ID of the monster cannot be changed and if it cannot be found,
    the update will not go through. This function will validate the
    fields of the monster object and will return true on successful update
    or false if something has gone wrong along with a console log of the error.
     */
    /**
     * Updates a monster's information in the database.
     * @return A boolean value correlated with the success of the database update.
     */
    public boolean update() {
        //TODO
        return false;
    }

    /*
    Takes in a Monster ID that must be deleted from the database. If the ID cannot
    be found, then the delete will fail.
     */
    /**
     * Deletes a monster from the database.
     * @return A boolean value correlated with the success of the database delete.
     */
    public boolean delete() {
        //TODO
        return false;
    }
}
