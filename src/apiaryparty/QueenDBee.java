package apiaryparty;

import java.util.Random;

/**
 * Benchmark Defender that only adds database honeypots
 * @author Oscar Veliz
 */


public class QueenDBee extends Defender{
		Random r;
	
	   public QueenDBee(String graphFile)
	   {
	        super("QeenDBee",graphFile);
	   }

	   @Override
		public void initialize() {
		   r = new Random();
		}

		@Override
		public void actionResult(boolean actionSuccess) {
			
		}

		@Override
		public DefenderAction makeAction() {
			int[] neighbors = new int[1];
			neighbors[0] = r.nextInt(net.getSize());
			if(getBudget() < honeypotCost(HoneypotType.DATABASE))
				return new DefenderAction(false);
			return new DefenderAction(HoneypotType.DATABASE,neighbors);
		}
}
