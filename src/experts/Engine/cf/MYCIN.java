/*
 * The MIT License
 *
 * Copyright 2018 owner.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package experts.Engine.cf;

import experts.Entities.Rule;

/**
 *
 * @author owner
 */
public class MYCIN implements ConfidenceFactor {

    @Override
    public String getUncertaintyTerm(Rule rule) throws Exception {
        if (rule.getCertaintyFactor() > 1 || rule.getCertaintyFactor() < -1)
            throw new Exception("CF out of range, should be at range [-1, 1]");
        return convert_(calculate_cf(rule));
    }

    @Override
    public float calculate_cf(Rule rule) {
        return rule.getCertaintyFactor() * get_minimum_cf(rule);
    }
    
    @Override
    public String convert_(float _cf) {
        if (_cf > 0.8)
            return "Definitely";
        else if (_cf > 0.6)
            return "Almost Certainly";
        else if (_cf > 0.4)
            return "Probably";
        else if (_cf > 0.2)
            return "Maybe";
        else if (_cf > -0.2)
            return "Unknown";
        else if (_cf >= -0.4)
            return "Maybe Not";
        else if (_cf >= -0.6)
            return "Probably Not";
        else if (_cf >= -0.8)
            return "Almost Certainly Not";
        else if (_cf >= -1)
            return "Definitely Not";
        return "";
    }
    
    private float get_minimum_cf(Rule rule) {
        float _cf_minim = rule.premises.get(0).getCertaintyFactor();
        for (int i = 1; i < rule.premises.size(); i++) {
            if (rule.premises.get(0).getCertaintyFactor() < _cf_minim)
                _cf_minim = rule.premises.get(i).getCertaintyFactor();
        }
        return _cf_minim;
    }
    
}
