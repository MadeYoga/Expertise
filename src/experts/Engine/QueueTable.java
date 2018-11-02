/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experts.Engine;

import experts.Entities.Premise;
import experts.Entities.Rule;
import java.util.ArrayList;

/**
 *
 * @author owner
 */
public class QueueTable {
    
    // CURRENT RULES PREMISES 
    public ArrayList <Premise> premises = new ArrayList <Premise>();
    
    public Rule current_rule = new Rule();
    
}
