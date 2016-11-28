/**
 * Created by Raul on 11/27/2016.
 */


/**
 * Network class: contains the nodes adjacent to it
 * Budget = Parser.ParseAttackBudget(attackname, defendname, graph)
 * To Attack: use AttackAction(AttackerActionType, nodeId)
 * Parameters: all the costs (Attaack, SuperAttack, Probe Point, Probe Honey Pot)
 */
package apiaryparty;

public class SupermanPrime extends Attacker{
    private final static String attackerName = "SupermanPrime";

    public SupermanPrime(String defenderName, String graphFIle){super(attackerName, defenderName, graphFIle);}
    public SupermanPrime(){super(attackerName);}

    @Override
    protected void initialize() {

    }

    @Override
    public AttackerAction makeAction() {
        return null;
    }

    @Override
    protected void result(Node lastNode) {

    }
}
