package apiaryparty;

import java.util.Random;

/**
 * Defender agent. The actions for the defender in this game include strengthening nodes, adding firewalls, and adding honeypots.
 * All logic/equations/formulas/etc for how your defender decides to select actions should be included in run()
 */
public abstract class Defender
{
    protected Network net;
    private DefenderMonitor dm;
    protected String defenderName;
    protected String graph;
    private DefenderAction lastAction;
    /**
     * Constructor.
     * Parses Network stored in graphFile.
     * Performs Defender logic to select actions.
     * Outputs [agentName]-[graphFile].defense with selected actions
     * @param agentName Defender agent's name i.e. "Miners"
     * @param graphFile String containing number of network i.e. "1914"
     */
    public Defender(String agentName, String graphFile)
    {
        defenderName = agentName;
        graph = graphFile;
        net = Parser.parseGraph(graphFile+".graph");
        dm = new DefenderMonitor(net,graphFile, agentName);
    }
    
    /**
     * Used for any initializations
     */
    public abstract void initialize();
    
    /**
     * GameMaster uses this method to determine the last action of the player
     * @return last action the player has made
     */
    public DefenderAction getLastAction(){
    	return lastAction;
    }
    
    /**
     * Notifies the defender if their last action was successful and allows for computations
     * based off result of last action.
     * @param actionSuccess the action's success
     */
    public abstract void actionResult(boolean actionSuccess);
    
    /**
     * This method forces the subclass player to make a single action (pickup or move a card).
     * Returning a null/invalid action will result in a wasted turn.
     */
    public void handleAction(){
    	lastAction = null;
        DefenderAction a = makeAction();
        if(a != null){
            switch(a.getType()){
            case STRENGTHEN:
            	if(dm.isValidStrengthen(a.getSNode())){
            		dm.strengthen(a.getSNode());
            		lastAction = a;
            		return;
            	}else{
            		dm.invalid();
            		lastAction = new DefenderAction(); //INVALID MOVE
            		return;
            	}
            case FIREWALL:
            	if(dm.isValidFirewall(a.getFwall1(), a.getFwall2())){
            		dm.firewall(a.getFwall1(), a.getFwall2());
            		lastAction = a;
            		return;
            	}else{
            		dm.invalid();
            		lastAction = new DefenderAction(); //INVALID MOVE
            		return;
            	}
            case HONEYPOT:
            	Random r = new Random();
            	int sv = 0;
            	int pv = 0;
            	switch(a.getHPType()){
            	case NETWORKED_CONVIENCE:
            		sv = r.nextInt(8) + 1;
            		pv = r.nextInt(8) + 1;
            	case PERSONAL_DEVICE:
            		sv = r.nextInt(11) + 5;
            		pv = r.nextInt(11) + 5;
            		break;
            	case SECURED_DEVICE:
            		sv = r.nextInt(8) + 12;
            		pv = r.nextInt(8) + 12;
            		break;
            	case DATABASE:
            		sv = r.nextInt(8) + 12;
            		pv = r.nextInt(10) + 20;
            		break;
            	}
            	boolean isDB = a.getHPType() == HoneypotType.DATABASE;
            	if(dm.isValidHoneypot(a.getNeighbors(),a.getHPType())){
            		dm.honeypot(sv, pv, isDB, a.getNeighbors());
            		lastAction = a;
            		return;
            	}else{
            		dm.invalid();
            		lastAction = new DefenderAction(); //INVALID MOVE
            		return;
            	}
            case END_TURN:
            	lastAction = new DefenderAction(false);//end turn
            	return;
            default:
            	dm.invalid();
        		lastAction = new DefenderAction(); //INVALID MOVE
        		return;
            }
        }
    }
    
    protected boolean isValidStrengthen(int node){
    	return dm.isValidStrengthen(node);
    }
    
    protected boolean isValidFirewall(int node1, int node2){
    	return dm.isValidFirewall(node1, node2);
    }
    
    protected boolean isValidHP(HoneypotType hpt, int[] neighbors){
    	return dm.isValidHoneypot(neighbors,hpt);
    }

    /**
     * Get Agent Name used by GameMaster.
     * @return Name of defender
     */
    public final String getName()
    {
        return defenderName;
    }

    /**
     * Get Game used by GameMaster
     * @return graph number
     */
    public final String getGraph()
    {
        return graph;
    }

    /**
     * Defender logic goes here
     */
    public abstract DefenderAction makeAction();
    
    /**
     * Returns remaining defender budget
     * @return the budget
     */
    public int getBudget(){
    	return dm.getBudget();
    }
    
    /**
     * Ends game
     */
    public void endGame(){
    	dm.endGame();
    	dm.close();
    }
    
    /**
     * Computes the cost of a honeypot based on the type
     * @param ht honeypot type
     * @return the cost of the honeypot
     */
    public int honeypotCost(HoneypotType ht){
    	switch(ht){
    		case NETWORKED_CONVIENCE:
    			return Parameters.HONEYPOT_RATE;
    		case PERSONAL_DEVICE:
    			return Parameters.HONEYPOT_RATE*2;
    		case SECURED_DEVICE:
    			return Parameters.HONEYPOT_RATE*3;
    		case DATABASE:
    			return Parameters.HONEYPOT_RATE*5;
    		default:
    			return Parameters.INVALID_RATE;
    			
    	}
    }
    

}
