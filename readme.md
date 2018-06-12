Fuzzy Logic demo for Red Hat Decision Manager v7.0
==================================================

Fuzzy Set definition:

	FuzzySet ageSet = new FuzzySet();
	ageSet.add(new FuzzyTrapezoid("young", 0, 0, 20, 31));
	ageSet.add(new FuzzyTrapezoid("mature", 14, 24, 60, 66));
	ageSet.add(new FuzzyTrapezoid("old", 53, 64, 150, 150));

Define it Global:

	ksession.setGlobal("ageSet", ageSet);

Insert the facts.
Fire the rules.

Example of fuzzy rule:

    rule "fuzzy young"
    
        when
            $p : Person ( ageSet.is( age, "young") )
            
        then
            System.out.println( $p.getName() + " is young" );
    
    end




