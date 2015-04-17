package trap;

import items.Item;
import stance.StandingOver;
import global.Global;
import combat.Combat;
import combat.Encounter;

import characters.Attribute;
import characters.Character;

public class Tripline implements Trap {
	private Character owner;
	@Override
	public void trigger(Character target) {
		if(target.human()){
			if(!target.check(Attribute.Perception, 20-target.get(Attribute.Perception))){
				Global.gui().message("You trip over a line of cord and fall on your face.");
				target.pain(null, 5);
				target.location().opportunity(target,this);
			}
			else{
				Global.gui().message("You spot a line strung across the corridor and carefully step over it.");
				target.location().remove(this);
			}
		}
		else{
			if(!target.check(Attribute.Perception, 15)){
				if(target.location().humanPresent()){
					Global.gui().message(target.name()+" carelessly stumbles over the tripwire and lands with an audible thud.");
				}
				target.pain(null, 5);
				target.location().opportunity(target,this);
			}
		}
	}

	@Override
	public boolean decoy() {
		return false;
	}

	@Override
	public boolean recipe(Character owner) {
		return owner.has(Item.Rope);
	}

	@Override
	public String setup(Character owner) {
		this.owner=owner;
		owner.consume(Item.Rope, 1);
		return "You run a length of rope at ankle height. It should trip anyone who isn't paying much attention.";
	}

	@Override
	public Character owner() {
		return this.owner;
	}
	public String toString(){
		return "Tripline";
	}

	@Override
	public boolean requirements(Character owner) {
		return true;
	}
	@Override
	public void capitalize(Character attacker, Character victim, Encounter enc) {
		enc.engage(new Combat(attacker,victim,attacker.location(),new StandingOver(attacker,victim)));
		victim.location().remove(this);
	}
	@Override
	public void resolve(Character active) {
		if(active!=owner){
			trigger(active);
		}
	}
}