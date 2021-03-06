package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;

public class Footjob extends Skill {

    public Footjob(Character self) {
        super("Footjob", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 22;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return (target.hasDick() || target.hasPussy()) && c.getStance().feet(getSelf()) && target.crotchAvailable()
                        && c.getStance().prone(getSelf()) != c.getStance().prone(target) && getSelf().canAct()
                        && !c.getStance().inserted() && getSelf().outfit.hasNoShoes();
    }

    @Override
    public float priorityMod(Combat c) {
        BodyPart feet = getSelf().body.getRandom("feet");
        Character other = c.p1 == getSelf() ? c.p2 : c.p1;
        BodyPart otherpart = other.hasDick() ? other.body.getRandomCock() : other.body.getRandomPussy();
        if (feet != null) {
            return (float) Math.max(0, feet.getPleasure(getSelf(), otherpart) - 1);
        }
        return 0;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 15;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(this, c, accuracy(c))) {
            int m = 8 + Global.random(6);
            if (getSelf().human()) {
                c.write(getSelf(), Global.format(deal(c, m, Result.normal, target), getSelf(), target));
            } else if (target.human()) {
                c.write(getSelf(), Global.format(receive(c, m, Result.normal, target), getSelf(), target));
            }
            if (target.hasDick()) {
                target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("cock"), m, c, this);
            } else {
                target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandom("pussy"), m, c, this);
            }
            if (Global.random(100) < 15 + 2 * getSelf().get(Attribute.Fetish)) {
                target.add(c, new BodyFetish(target, getSelf(), "feet", .25));
            }
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), Global.format(deal(c, 0, Result.miss, target), getSelf(), target));
            } else if (target.human()) {
                c.write(getSelf(), Global.format(receive(c, 0, Result.miss, target), getSelf(), target));
            }
            return false;
        }

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Footjob(user);
    }

    @Override
    public int speed() {
        return 4;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You attempt to place your foot between " + target.nameOrPossessivePronoun() + " legs, but "
                            + target.pronoun() + " moves away at the last second.";
        } else {
            String message = "";
            if (target.hasDick()) {
                message = "You press your foot against {other:name-possessive} girl-cock and stimulate it by rubbing it up and down with the sole of your foot, occasionally teasing the head with your toes. {other:POSSESSIVE} {other:body-part:cock}";
                if (target.getArousal().percent() < 30) {
                    message += "starts to get hard.";
                } else if (target.getArousal().percent() < 60) {
                    message += "throbs between your soles.";
                } else {
                    message += "is practically leaking pre-cum all over your soles.";
                }
            }
            if (target.hasPussy()) {
                message = "You rub your foot against " + target.name()
                                + "'s pussy lips while rubbing {other:possessive} clit with your big toe. ";
                if (target.getArousal().percent() < 30) {
                    message += "The wetness from {other:possessive} excitement starts to coat the underside of your foot.";
                } else if (target.getArousal().percent() < 60) {
                    message += "{other:POSSESSIVE} {other:body-part:pussy} is so wet, your foot easily glides along {other:possessive} parted lips.";
                } else {
                    message += "{other:PRONOUN} is so wet that your toes briefly slip inside of {other:direct-object} before pulling them out to tease {other:direct-object} further.";
                }
            }
            return message;
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name() + " swings her foot at your groin, but misses.";
        } else {
            if (target.hasDick()) {
                return getSelf().name()
                                + " rubs your dick with the sole of her soft foot. From time to time, she teases you by pinching the glans between her toes and jostling your balls.";
            }
            if (target.hasPussy()) {
                return getSelf().name()
                                + " toes the lips of your slit with her foot. From time to time, she teases you by slipping her big toe inside and wiggling it around.";
            }
            return getSelf().name()
                            + " toes your asshole with her foot. From time to time, she teases you by pressing her big toe at your sphincter and nudging it.";
        }
    }

    @Override
    public String describe(Combat c) {
        return "Pleasure your opponent with your feet";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
