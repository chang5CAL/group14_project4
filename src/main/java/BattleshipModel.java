/**
 * Created by root on 2/2/17.
 */
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class BattleshipModel {

    private Board ai;
    private Board player;

    //public void play(){}

    public BattleshipModel(){
        //This constructor is so the boards are initialized so they can be used.
        ai = new Board();
        player = new Board();
    }

    public boolean gameOver(){
        //This causes an error so I'm just gonna leave this here for now -Justin
        return false;
    }
    //Why does this even ask for the board? It should know it already!
    public void aiFire(Coordinate shot){
        //get array of all occupied coordinates on ai board
        ArrayList<Coordinate> ships = player.getAllShips();
        //for each cordinate
        for(Coordinate occupied: ships){
            //if the shot matches a ship location
            if(shot.getAcross() == occupied.getAcross() &&
                    shot.getDown() == occupied.getDown()){
                //add to ai's hit list
                player.addHit(shot);
                return;
            }
        }
        //If it is not occupied then add to miss list
        player.addMiss(shot);

    }

    public void fire(Coordinate shot){
        //get array of all occupied coordinates on ai board
        ArrayList<Coordinate> ships = ai.getAllShips();
        //for each cordinate
        for(Coordinate occupied: ships){
            //if the shot matches a ship location
            if(shot.getAcross() == occupied.getAcross() &&
                    shot.getDown() == occupied.getDown()){
                //add to ai's hit list
                ai.addHit(shot);
                return;
            }
        }
        //If it is not occupied then add to miss list
        ai.addMiss(shot);

    }

    public void placeShipAI(){
        generateShipLocation(ai,"Clipper",3);
        generateShipLocation(ai,"Submarine",3);
        generateShipLocation(ai,"Dinghy",1);
        generateShipLocation(ai,"Battleship",4);
        generateShipLocation(ai,"Carrier",5);
    }

    //This could either go in a
    public void placeShipAIEasy(){
        //This could probably be shortened, but this should be fine.
        Coordinate start = new Coordinate(3,3);
        Coordinate end = new Coordinate(3,5);
        Ship s = new Civillianship("Clipper",3,start,end,"vertical",true);
        ai.addShip(s);

        start = new Coordinate(5,3);
        end = new Coordinate(5,5);
        s = new Battleship("Submarine",3,start,end,"vertical",false);
        ai.addShip(s);

        start = new Coordinate(9,5);
        end = new Coordinate(9,5);
        s = new Civillianship("Dinghy",1,start,end,"vertical",true);
        ai.addShip(s);

        start = new Coordinate(2,6);
        end = new Coordinate(5,6);
        s = new Battleship("Battleship",4,start,end,"horizontal",true);
        ai.addShip(s);

        start = new Coordinate(1,1);
        end = new Coordinate(5,1);
        s = new Battleship("Carrier",5,start,end,"horizontal",true);
        ai.addShip(s);
    }

    public void aiFireEasy(){
        //Pattern: Just shoot row by row.
        int turn = player.getMisses().size()+player.getHits().size();
        int x = turn/10+1;
        int y = turn%10+1;
        Coordinate c = new Coordinate(x,y);
        aiFire(c);

    }

    public void placeShip(Ship ship){
        player.addShip(ship);
    }
  
    public void generateShipLocation(Board ai, String type, int size){
        int x,y,direction;
        Coordinate start = new Coordinate(0,0),end = new Coordinate(0,0);
        //This is because there's a syntax error if it's not assigned to begin with.
        boolean valid = true;
        direction = ThreadLocalRandom.current().nextInt(0,2);
        String dir;
        Ship s = null;
        if (direction == 0){
            //Facing sideways
            dir = "horizontal";
            while(valid){
                valid = false;
                //Wait. Does the grid go from 0-9, or 1 to 10? I think 1-10.
                x = ThreadLocalRandom.current().nextInt(1,11-size);
                y = ThreadLocalRandom.current().nextInt(1,11);
                start = new Coordinate(x,y);
                end = new Coordinate(start.getAcross()+size,start.getDown()+size);
                for(int i=0;i<ai.getShips().size();i++){
                    Coordinate[] comp = ai.getShips().get(i).getCoordinates();
                    for(int j=0;j<comp.length;j++){
                        //Make sure there is no intersection.
                        if((comp[j].getAcross() >= start.getAcross() && comp[j].getAcross() <= end.getAcross()) &&
                                (comp[j].getDown() >= start.getDown() && comp[j].getDown() <= end.getDown())){
                            //If the point is greater than the prospective point's start but less than it's end
                            //for both X and Y, that means there's a problem.
                            valid = true;
                        }
                    }

                }
            }

        }
        else {
            //Facing downwards
            dir = "vertical";
            while(valid){
                valid = false;
                x = ThreadLocalRandom.current().nextInt(1,11);
                y = ThreadLocalRandom.current().nextInt(1,11-size);
                start = new Coordinate(x,y);
                end = new Coordinate(start.getAcross()+size,start.getDown()+size);
                for(int i=0;i<ai.getShips().size();i++){
                    Coordinate[] comp = ai.getShips().get(i).getCoordinates();
                    for(int j=0;j<comp.length;j++){
                        //Make sure there is no intersection.
                        if((comp[j].getAcross() >= start.getAcross() && comp[j].getAcross() <= end.getAcross()) &&
                                (comp[j].getDown() >= start.getDown() && comp[j].getDown() <= end.getDown())){
                            //If the point is greater than the prospective point's start but less than it's end
                            //for both X and Y, that means there's a problem.
                            valid = true;
                        }
                    }

                }
            }

        }
        if (type == "Clipper"){
            s = new Civillianship(type,size,start,end,dir,true);
        } else if (type == "Submarine"){
            s = new Battleship(type,size,start,end,dir,false);
        } else if (type == "Dinghy"){
            s = new Civillianship(type,size,start,end,dir,true);
        } else if (type == "Battleship"){
            s = new Battleship(type,size,start,end,dir,true);
        } else if (type == "Carrier"){
            s = new Battleship(type,size,start,end,dir,true);
        }
        //Hard coded, but it's fine since this is all that's going to be inputted.

        ai.addShip(s);

    }

    public Board getAI(){
        //This is for testing purposes.
        return ai;
    }
    public Board getPlayer(){
        return player;
    }
}
