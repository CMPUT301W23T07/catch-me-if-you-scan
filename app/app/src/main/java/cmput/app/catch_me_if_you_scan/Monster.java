package cmput.app.catch_me_if_you_scan;

public class Monster {

    String name;
    String hexHash;

    Monster(String name){
        this.name = name;
        this.hexHash = "b9dd960c1753459a78115d3cb845a57d924b6877e805b08bd01086ccdf34433c";
    }

    public String getName(){
        return this.name;
    }

    public String getHexHash(){
        return this.hexHash;
    }
}
