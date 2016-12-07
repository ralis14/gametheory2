/**
 * Created by Raul on 11/27/2016.
 */


/**
 * Network class: contains the nodes adjacent to it
 * Budget = Parser.ParseAttackBudget(attackname, defendname, graph)
 * To Attack: use AttackAction(AttackerActionType, nodeId)
 * Parameters: all the costs (Attack, SuperAttack, Probe Point, Probe Honey Pot)
 */
package apiaryparty;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.util.ArrayList;

public class SupermanPrime extends Attacker{
    private final static String attackerName = "SupermanPrime";

    private int count = 0;

    public SupermanPrime(String defenderName, String graphFIle){super(attackerName, defenderName, graphFIle);}
    public SupermanPrime(){super(attackerName);}

    @Override
    protected void initialize() {

    }

    @Override
    public AttackerAction makeAction() {
        final boolean  DBUG = true;
        int move = 0;
        int nodeId = 0;
        AttackerActionType type;

        /*
        ************************************************************
        choose what node below
        ************************************************************
         */

        Node n = chooseNode(availableNodes.get(0));
        //Node n = availableNodes.get(0);

        /*
        *************************************************************
        check captured status and re-choose node or choose move
        *************************************************************
         */


//        boolean captured = n.isCaptured();
//        if(captured){n = chooseNode();}
//        else{
//            move = chooseMove(n);
//        }


        move = chooseMove(n);
        if(count>0)
            move =0;
        switch(move){
            case 0:
                type = AttackerActionType.ATTACK;
                //dBString(DBUG, n);
                break;
            case 1:
                type = AttackerActionType.SUPERATTACK;
                break;
            case 2:
                type = AttackerActionType.PROBE_POINTS;
                //count ++;
                //nodeId = n.getNodeID();
                break;
            case 3:
                type = AttackerActionType.PROBE_HONEYPOT;
                break;
            default:
                type = AttackerActionType.INVALID;
                break;
        }
        return new AttackerAction(type, n.getNodeID());
    }

    @Override
    protected void result(Node lastNode) {

    }

    private int chooseMove(Node n){
        /*
        ************************************************
        most variables should be initialized once, so do it outside the method
        1. Check Point Value
        2. Check HoneyPot Status
        3.
        ***********************************************
         */
        boolean isDB = n.isDatabase();
        boolean knowHP = n.knowsHoneyPot();
        int knowPV = n.getPv();
        int sv = n.getSv();
        boolean isHoneyPot = true;
        int hp_number = 0;
        boolean hasHP = checkForHP(hp_number);

        //if true super attack is worth is else it is not
        boolean superRatio = superToNormalRatio();

        //Check HoneyPot Status
        if(knowHP){
            isHoneyPot = n.isHoneyPot();
        }
        //Super Attack if DB and !HP
        if(superRatio || (n.getSv() > Parameters.ATTACK_ROLL)) {
            if ((isDB && !isHoneyPot) || (n.getSv() > Parameters.ATTACK_ROLL))
                return 1;
        }
        //if it has hps probe nodes
        if(!knowHP && hasHP){
            if(Parameters.PROBE_HONEY_RATE < 5 || ((Parameters.HONEYPOT_RATE < 25) && Parameters.PROBE_HONEY_RATE < 5)){
                return 3;
            }
        }
        //Probe Points
        if(Parameters.PROBE_POINTS_RATE < 1 && (n.getPv() != -1)){return 2;}

        //default is simple attack
        else{
            return 0;
        }

    }

    private Node chooseNode(Node n){
        if((n.knowsHoneyPot() && n.isHoneyPot()) || n.isCaptured()){
            for(Node x: availableNodes){
                if(!x.isCaptured() || (x.knowsHoneyPot() && !x.isHoneyPot())){
                    return x;
                }
            }
        }
        return n;
    }

    private boolean superToNormalRatio(){
        float attack = Parameters.ATTACK_RATE;
        float superAttack  = Parameters.SUPERATTACK_RATE;
        float baseRatio = 40;
        float answer = 0;
        answer = (attack * 100)/superAttack;
        if(answer >= 40f)
            return true;
        else
            return false;



    }

    private boolean checkForHP(int hp_number){
        int size = availableNodes.size();
        if(size> Parameters.NUMBER_OF_NODES) {
            hp_number = size - Parameters.NUMBER_OF_NODES;
            return true;
        }
        else return false;
    }

    private void dBString(boolean DBUG, Node n){
        if(DBUG){
            String name = "Superman Prime";
            int size = availableNodes.size();

            System.out.printf('\n' + "Name is: %s"+
                    "\n" +"Size is: %s" +
                    "\n" + "Budget before %s" +
                    "\n" + "PV: %s" +
                    "\n" + "SV: %s" +
                    "\n" + "%s", name, size, budget, n.getPv(), n.getSv(), "**********************"
            );
            System.exit(0);
        }
        System.out.println("\n" + "Node Size " + availableNodes.size());
    }
}
