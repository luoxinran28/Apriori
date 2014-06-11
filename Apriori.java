import java.awt.List;
import java.util.*;



public class Apriori {

	private final static int SUPPORT = 2;
    private final static double CONFIDENCE = 0.7;
    private final static String SPLIT_SIGN=";";
    

	
    public Map<String, Integer> start(Map<String, String> Transaction){
    	
    	Map<String, Integer> LargeItemSets_k = new HashMap<String,Integer>();
        Map<String,Integer> FrequentLargeItems=new HashMap<String,Integer>();
        Map<String,Integer> NOTFrequentLargeItems=new HashMap<String,Integer>();
    	
      
    	// get large k-itemsets
		for(LargeItemSets_k.putAll(getLargeItemStes_1(Transaction));
				LargeItemSets_k != null && LargeItemSets_k.size() != 0; ){
			
			// New candidates
			Map<String, Integer> CandidateItemSets_k = getCandidateItemSets_k(LargeItemSets_k,NOTFrequentLargeItems);
//		    System.out.println("This is CandidateItemSets_k:"+CandidateItemSets_k);

			for(String transc : Transaction.values()){

//            	System.out.println("********************");
//				System.out.println("Transaction: "+ transc + " in this loop.");
				for(String candidate : CandidateItemSets_k.keySet()){
//					System.out.println("Candidate: "+ candidate + " in this loop.");
					Integer countContains = 0;
					String[] candidateItems=candidate.split(SPLIT_SIGN);
                    for(int i = 0; i<candidateItems.length; i++){
                    	
	                    
                    	// check whether the candidates are in transaction
	                    // one candidate contained in transaction
	                    if(transc.indexOf(candidateItems[i]) != -1){
	                    	countContains++;
						}
	                    // all candidates in transaction
	                    if(countContains == candidateItems.length)
	                    {
	                    	Integer count = CandidateItemSets_k.get(candidate);
	                    	CandidateItemSets_k.put(candidate, count+1);
//	                    	System.out.println("The candidate:  "+ candidate + 
//									"IN Transaction: "+ transc);
	                    }
						
	                    
	                    
                    }
					
				}
				
			}
//			System.out.println("This is CandidateItemSets_k:"+CandidateItemSets_k);
			
			// delete the itemset in terms of SUPPORT
			FrequentLargeItems.clear();
			FrequentLargeItems.putAll(LargeItemSets_k);
			LargeItemSets_k.clear();
			for(String candidate : CandidateItemSets_k.keySet()){
				
				Integer countInteger = CandidateItemSets_k.get(candidate);
				
				if(countInteger >= SUPPORT){
					LargeItemSets_k.put(candidate, countInteger);
				}
				else {
					NOTFrequentLargeItems.put(candidate, countInteger);
				}
				
			}
			
//			System.out.println("This is LargeItemSets_k:"+LargeItemSets_k);
			
			
	        FrequentLargeItems.putAll(LargeItemSets_k);
		}
		
//		System.out.println("_____________________________");
//    	System.out.println("The final Result is:  "+ FrequentLargeItems);
		return FrequentLargeItems;
    }
    
    
    // get the first large itemsets
    private Map<String, Integer> getLargeItemStes_1(Map<String, String> Transaction){
    	
      	Map<String, Integer> SplitItems = new HashMap<String,Integer>();
      	Map<String, Integer> ReturnItems = new HashMap<String,Integer>();
      	
      	for(String transc : Transaction.values()){
      		String[] Items = transc.split(SPLIT_SIGN);
      		System.out.println("Items: "+transc);
      		for(String Item : Items){
      			
      			Integer count = SplitItems.get(Item);

      			if(count == null){
      				SplitItems.put(Item, 1);
      			}
      			else {
					SplitItems.put(Item, count+1);
				}
      			
      		}
      		
      	}
      	

	
  		for(String splitem : SplitItems.keySet()){
  		
      		Integer count = SplitItems.get(splitem);
      		if(count >= SUPPORT){
      			ReturnItems.put(splitem, count);
      		}
      		
      	}
//  		System.out.println("L1:");
//  		System.out.println(ReturnItems);
//  		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
      	return ReturnItems;
    }
    
    
    // get the k candidate
    
    private Map<String, Integer> getCandidateItemSets_k( Map<String, Integer> LargeItemSets_k,
    														Map<String,Integer> NOTFrequentLargeItems){

    	ArrayList<String> NewCandidate = new ArrayList<String>();
    	Map<String, Integer> ReturnItems = new HashMap<String,Integer>();
    	
    	// select p.item1, p.item2, ..., p.itemk-1, q.itemk-1
    	for (String p : LargeItemSets_k.keySet()) {
			
    		for (String q : LargeItemSets_k.keySet()) {
				
    			// from Lk-1 p, Lk-1 q
                String[] Items1 = p.split(SPLIT_SIGN);
                String[] Items2 = q.split(SPLIT_SIGN);
                String JoinResult = "";
                
                
                if (Items1.length == 1) {

                	
                	// where p.itemk-1 < q.itemk-1
                	if (Items1[0].compareTo(Items2[0]) < 0) {
                		
                		JoinResult = Items1[0] + SPLIT_SIGN+Items2[0] + SPLIT_SIGN;
                		NewCandidate.add(JoinResult);
//                		System.out.println("first JoinResult: "+JoinResult);
					}
                	
                	
				} else {
					

					
                    // where p.item1 = q.item1, ... , p.itemk-2 = q.itemk-2, 
					//       p.itemk-1 < q.itemk-1
					Integer countEquals = 0;
			        for(int i=0;i<Items1.length;i++){

			        	if(Items1[i].equals(Items2[i])){
			        		countEquals++;
			        	}
		        		if( i == Items1.length-1
		        				&& countEquals == Items1.length-1){ // first i Items equal.
			        		if(Items1[i].compareTo(Items2[i]) < 0){
								JoinResult = p + Items2[i] + SPLIT_SIGN;
								NewCandidate.add(JoinResult);
//								System.out.println("rest JoinResult: "+JoinResult);
							}
			        		
			        	}
			         }

			        
			         
				}
                
			}
    		
		}		
	        	
    	
    	

		// prune
    	/*
    	 *  find the candidates which are not in NOT-FREQUENT items
    	 * 
    	 * */
//		System.out.println("start to prune: ");
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");


		// for each new candidate
		Iterator<String> NewCItr = NewCandidate.iterator();
//		System.out.println("New Candidate is: "+NewCandidate);
		Integer count = 0;
		while (NewCItr.hasNext()) {
			
			String newCandiString = NewCItr.next();

		    
		    boolean isSubItems = true;
		    
		    	for (String NotFreqItems : NOTFrequentLargeItems.keySet()) {
//		    		System.out.println("NOT Frequent Items are: "+NotFreqItems);
		    		String[] NFqItem = NotFreqItems.split(SPLIT_SIGN);
		    		// for each items in each LargeItems
		    		for(int i = 0; i < NFqItem.length; i++){
		    			
//		    			System.out.println("Not Frequent Item detail is: "+NFqItem[i]);
//    	    		    System.out.println("Candidate is: "+newCandiString);
        		    	

    	    		    
//    	    		    Litems[i].equals(NewCitems[j])
        		    	if (newCandiString.contains(NFqItem[i])){
        		    		count++;
//        		    		System.out.println("count: "+count);
//        		    		System.out.println("candidate item "+NFqItem[i]
//        		    								+" contain NOT frequent Item "+NFqItem[i]);
        		    	}
        		    	else {
							count = 0;
							break;
						}


        		    	
        		    	
		    		}
		    		
		    		// find the same items in large set
    		    	if (count == NFqItem.length) {
    		    		System.out.println("count: "+count);
    		    		count = 0;
    		    		isSubItems = false;
    		    		break;
					}
		    	}
		    	
		    
		    if(isSubItems){
//		    	System.out.println("NewCItr.next(): "+newCandiString);
		    	
		    	ReturnItems.put(newCandiString, 0);
//		    	System.out.println("ReturnItems: "+ReturnItems);
//		    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
		    	isSubItems = false;
		    }
		    
		    
		    
		}
	
//    	System.out.println("ReturnItems: "+ReturnItems);
        	
    	
    	

    	return ReturnItems;
    	
    }

	
    
    public static void main(String[] args){
    	
        // datas
        Map<String, String> Transaction = new HashMap<String, String>();

    	Transaction.put("T100", "1;2;5;");
    	Transaction.put("T101", "2;4;");
    	Transaction.put("T102", "2;3;");
    	Transaction.put("T103", "1;2;4;");
    	Transaction.put("T104", "1;3;");
    	Transaction.put("T105", "2;3;");
    	Transaction.put("T106", "1;3;");
    	Transaction.put("T107", "1;2;3;5;");
    	Transaction.put("T108", "1;2;3;");
    	
    	for(Map.Entry<String,String> mapping : Transaction.entrySet()){
//    		System.out.println(mapping);
    	}
        
        
    	
    	Apriori apriori = new Apriori();
    	Map<String, Integer> result = apriori.start(Transaction);
    	System.out.println("****************");
    	System.out.println("The result is: ");
    	System.out.println(result);
    	

    }
	

}