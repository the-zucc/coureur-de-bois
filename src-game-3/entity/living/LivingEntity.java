package entity.living;

import java.util.ArrayList;

import characteristic.*;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Positionnable;
import characteristic.positionnable.Reachable;
import entity.GravityAffectedCollidingEntity;
import entity.living.human.Player;
import entity.statics.tree.Tree;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import util.StopCondition;
import visual.Component;

public abstract class LivingEntity extends GravityAffectedCollidingEntity implements ComponentUpdateable {

    //private Point3D gravity;
    protected double hp;
    protected double maxHp;
    protected boolean up;
    protected boolean down;
    protected boolean left;
    protected boolean right;
    private boolean newOrientation;
    private boolean isRunning;
    private Point3D oldMovement;
    private Point3D jumpVector = new Point3D(0, -40, 0);
    private Point2D target;
    protected double rotationAngle;
    private boolean isAtTarget = false;
    private Positionnable targetEntity = null;

    protected void setTargetEntity(Positionnable p) {
        this.targetEntity = p;
    }

    protected Positionnable getTargetEntity() {
        return targetEntity;
    }

    protected boolean isAtTarget() {
        if (this.targetEntity != null && this.targetEntity instanceof Collideable) {
            if (isAtTarget) {
                isAtTarget = false;
                return true;
            } else {
                if (this.targetEntity instanceof Reachable) {
                    if (((Reachable) this.targetEntity).getReachableRadius() < this.distanceFrom(targetEntity)) {
                        return true;
                    }
                }
            }
        }
        return this.position2D.distance(this.target) < GameLogic.getMeterLength();
    }

    protected double computeComponentRotationAngle(double rotationAngle) {
        return rotationAngle + 90;
    }

    public LivingEntity(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        up = down = left = right = newOrientation = isRunning = false;
        oldMovement = Point3D.ZERO;
        movement = oldMovement;
        target = null;
        rotationAngle = 0;
        hp = 100;
        maxHp = hp;

        accept("damage", (params) -> {
            if (params[0] == this) {
                Double amount = (Double) params[1];
                takeDamage(amount, (MessageReceiver) params[2]);
            }
        });
    }

    protected abstract double computeXpReward();

    @Override
    public boolean shouldUpdate() {
        return true;
    }

    @Override
    public void update(double secondsPassed) {
        updateMovementVector();
        processFlinch();
        super.update(secondsPassed);
        executeMainAction();
        updateActions(secondsPassed);
        if (shouldUpdateComponent())
            updateComponent();
    }

    public abstract void updateActions(double secondsPassed);

    @Override
    public boolean shouldUpdateComponent() {
        return true;
    }

    @Override
    public void updateComponent() {
        getComponent().setPosition(getPosition());
        getComponent().setRotationAxis(Rotate.Y_AXIS);
        getComponent().setRotate(computeComponentRotationAngle(rotationAngle));
        additionalComponentUpdates();
    }

    public abstract void additionalComponentUpdates();

    public double getHp() {
        return hp;
    }

    protected void updateMovementVector() {
        oldMovement = movement;

        double movementSpeed = computeMovementSpeed();
        double angle = Math.toRadians(45);
        if (target == null) {
            if (up) {
                if (up && right) {
                    movement = new Point3D(Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
                } else if (up && left) {
                    movement = new Point3D(-Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
                } else {
                    movement = new Point3D(0, 0, movementSpeed);
                }
            } else if (down) {
                if (down && right) {
                    movement = new Point3D(Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
                } else if (down && left) {
                    movement = new Point3D(-Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
                } else {
                    movement = new Point3D(0, 0, -movementSpeed);
                }

            } else if (right)
                movement = new Point3D(movementSpeed, 0, 0);
            else if (left)
                movement = new Point3D(-movementSpeed, 0, 0);
            else
                movement = new Point3D(0, 0, 0);
            if (oldMovement.angle(Rotate.X_AXIS) == movement.angle(Rotate.X_AXIS))
                newOrientation = false;
            else
                newOrientation = true;
        } else {
            if (position2D.distance(target) < movement.magnitude()) {
                movement = Point3D.ZERO;
                target = null;
            } else {
                Point3D temp = new Point3D(target.getX(), 0, target.getY()).subtract(new Point3D(position2D.getX(), 0, position2D.getY())).normalize();
                movement = temp.multiply(1 / temp.magnitude()).multiply(movementSpeed);
            }

        }
        rotationAngle = computeAngleFromMovement(movement);
    }

    /**
     * this method should be defined in all subclasses. it returns the movement speed to use. The value can be dynamic, as the method is called every tick.
     *
     * @return the movement speed to use when computing the movmement vector for this entity.
     */
    protected abstract double computeMovementSpeed();

    protected double computeAngleFromMovement(Point3D movement) {
        if (movement.equals(Point3D.ZERO))
            return rotationAngle;
        double angle = new Point3D(movement.getX(), 0, movement.getZ()).angle(Rotate.X_AXIS);
        if (movement.getZ() > 0)
            angle *= -1;
        return angle;
    }

    protected void setUp(boolean value) {
        up = value;
        newOrientation = true;
    }

    protected void setDown(boolean value) {
        down = value;
        newOrientation = true;
    }

    protected void setLeft(boolean value) {
        left = value;
        newOrientation = true;
    }

    protected void setRight(boolean value) {
        right = value;
        newOrientation = true;
    }

    protected void setIsRunning(boolean value) {
        isRunning = value;
    }

    protected boolean isRunning() {
        return isRunning;
    }

    protected void jump() {
        if (getPosition().getY() >= map.getHeightAt(get2DPosition()) - 0.1 * GameLogic.getMeterLength()) {
            this.addForceToGravity(getJumpVector());
        }
    }

    protected Point3D getJumpVector() {
        return jumpVector;
    }

    protected void startMovingTo(Point2D target) {
        this.target = target;
    }

    protected void takeDamage(double amount, MessageReceiver attacker) {
        hp -= amount;
        if (amount > 0) {
            if (attacker instanceof Positionnable) {
                flinch(((Positionnable) attacker).getPosition());
            } else {
                flinch(null);
            }
            if (hp <= 0) {
                Component c = getComponent();
                if (this instanceof Player) {
                    ((Player) this).detach(((Player) this).getGameCamera());
                }
                animator.animate(() -> {
                    c.getTransforms().add(new Rotate(1, Rotate.Z_AXIS));
                }, 50).done(() -> {
                    messenger.send("dead", this);
                    onDeath();
                });
            }
        } else {
            if (getHp() > maxHp) {
                hp = maxHp;
            }
        }
    }

    protected abstract void onDeath();

    private ArrayList<Point3D> flinchMovements = new ArrayList<Point3D>();
    private ArrayList<Point3D> flinchMovementsToSubtract = new ArrayList<Point3D>();
    private Point3D flinchMovement = null;

    protected void flinch(Point3D from) {
        if (from != null) {
            Point3D flinchMovement = getPosition().subtract(from).normalize();
            Point3D flinchMovementToSubtract = new Point3D(flinchMovement.getX() / 30, flinchMovement.getY() / 30, flinchMovement.getZ() / 30);
            flinchMovements.add(flinchMovement);
            flinchMovementsToSubtract.add(flinchMovementToSubtract);
        } else {
            jump();
        }
    }

    private void processFlinch() {
        for (int i = 0; i < flinchMovements.size(); i++) {
            Point3D flinchMovement = flinchMovements.get(i);
            Point3D flinchMovementToSubtract = flinchMovementsToSubtract.get(i);
            flinchMovement = flinchMovement.subtract(flinchMovementToSubtract);
            if (flinchMovement.getX() < flinchMovementToSubtract.getX()) {
                flinchMovements.remove(i);
                flinchMovementsToSubtract.remove(i);
                i--;
            } else {
                moveTo(getPosition().add(flinchMovement));
                flinchMovements.set(i, flinchMovement);
            }
        }
    }


    protected void attack(MessageReceiver target, double damage) {
        messenger.send("damage", target, damage, this);
    }

    protected void addXP(double xp) {

    }

    protected void updateOrientation() {

    }

    @Override
    public void onCollides(Collideable c) {
        if (c == this.targetEntity) {
            isAtTarget = true;
        }
    }

    protected Runnable mainAction;
    protected StopCondition mainActionStopCondition;
    protected Runnable mainActionCallback;

    protected void startMainAction(Runnable action, StopCondition condition, Runnable callback) {
        mainAction = action;
        mainActionStopCondition = condition;
        mainActionCallback = callback;
    }

    protected void executeMainAction() {
        if(mainAction != null){
            mainAction.run();
            if (mainActionStopCondition.shouldStop()) {
                mainActionCallback.run();
            }
        }
    }

    protected void cancelMainAction() {
	    mainAction = null;
	    mainActionStopCondition = null;
	    mainActionCallback = null;
    }
}
