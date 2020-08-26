# Ciege Usage and Documentation
#### Caffeine Game Engine

the caffeine game engine (or ciege) is a project I
have been working on for a while.
and I'm happy to be able to write this Documentation of the completed library.

But you probably already know what it is capable of, so let's get into it!
please note that this is a tutorial on how to create a game, but *not* a in-depth explanation of what's going on.
for that's just read the Documentation.

[Ciege Website](http://runningmanstudios.com/ciege)

#### How to start

it is relatively straightforward to create a game using Ciege.
but you need a few things that need to be done before we can start coding
* Knowledge of how Java works, I'm not teaching you how to program. there are plenty of youtube tutorials out there.
* You have to have a good ide or way to program (Intellij, Eclipse, ect.).
* You have to actually have the library inside your project (obviously)

and once you do that, you can start!

###### step 1: creating the window

Inside your main method, create an Instance of Game\
`Game myGameWindow = new Game(300, 300, "Ciege");`

now with this Game object we can edit our window,
```
myGameWindow.showFPS(Game.FPSDisplayMode.TITLE_BAR);
myGameWindow.show();
```
and if you run the program, you will see a black window on the screen.\
![Image of the result](https://i.imgur.com/eNDxYSr.png)

###### step 2: drawing to the window

this is where it starts to get complicated.
first let's add this at the end of the main method:
`myGameWindow.getSceneManager().switchScene(new Scene(){ });`\
what we're doing here is geting a SceneManager from our `myGameWindow` and switching the Scene.\
but oh no, our IDE is giving us an error. that's because we need to implement a few Methods and pass the SceneManager as an argument.
```
myGameWindow.getSceneManager().switchScene(new Scene(myGameWindow.getSceneManager()){
    @Override
    public void onCreate() {
        
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDraw(ScenicGraphics sg) {

    }
});
```

now let's go into the onDraw() method and do something with the ScenicGraphics.
first let's set the color.
`sg.setColor(Color.red);`\
and then let's draw a rectangle.
`sg.displayRect(50, 50, 100, 150, true, false);`\
in case your wondering what the parameters do,\
the first one is the x, the second one is the y,
third is the width and the fourth is the height.\
but the fifth is whether or not to fill the rectangle in or not. and the sixth is whether or not to center the rectangle on the x and y.

if that's confusing, try messing with the two booleans at the end and see what happens.

so let's run the program and see!\
![Image of the result](https://i.imgur.com/K6V3rzU.png)

if you've been following your code should look like this:
```java
public class Main {
    public static void main(String[] args){
        Game myGameWindow = new Game(300, 300, "Ciege");
        myGameWindow.showFPS(Game.FPSDisplayMode.TITLE_BAR);
        myGameWindow.show();
        myGameWindow.getSceneManager().switchScene(new Scene(myGameWindow.getSceneManager()){
            @Override
            public void onCreate() { }

            @Override
            public void onUpdate() { }

            @Override
            public void onDraw(ScenicGraphics sg) {
                sg.setColor(Color.red);
                sg.displayRect(50, 50, 100, 150, true, false);
            }
        });
    }
}
```

###### step 3: creating entities

if you read the Documentation then you know that Ciege works with entities.

to create an entity you override the AbstractEntity class\
so inside of our Scene let's add this just above the onCreate() method:
`AbstractEntity gerald;`\
and then put this *inside* the onCreate() method
```
this.gerald = new AbstractEntity(20, 20, 20, 20, getSceneManager()){
    @Override
    public void onCreate() {
        
    }
    @Override
    public void onUpdate() {

    }
    @Override
    public void onDraw(ScenicGraphics esg) {
        
    }
};
```
but what's this! gerald has his own onCreate() and onUpdate() methods!\
yes, almost all things you'll be working with in ciege will have their own onCreate() and onUpdate() methods.

so now let's draw him using his onDraw() method.
```
esg.setColor(Color.blue);
esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, false);
```
and if we run the program... what! nothing happened!
well before we can do anything with our entity we have to add him to the scene.\
so after you declare the entity add this line of code:
`addEntityToScene(this.gerald);`
now we can run the program and see a little blue guy.

![Gerald sitting peacfully](https://i.imgur.com/omyvIm6.png)

###### step 4: moving entities

this step is very short but inside gerald's onUpdate() method add this bit of code:
```
setX(getX() + 1);
setY(getY() + 1);
```

all this does is setting gerald's position to the current position of gerald plus one.
and if we run this. you can see gerald running on the screen at top speed! look at the fella go!!!

![Gerald running across the screen](https://i.imgur.com/wMU2Owo.png)

###### step 5: user input

now let's comment out the setX() and setY() methods we called earlier and type this:
`Input userInput = myGameWindow.getInput();`\
The Input class is responsible for getting mouse and keyboard input from the user.
so now that we have the userInput let's detect of the user presses the "S" key, like so:
```
if (userInput.isKeyPressed(KeyEvent.VK_S)){

}
```
and inside this if statement let's move gerald down:
`setY(getY() + 1);`

and now let's run the program and watch as gerald follows our every command...

ok, this is kind of boring. so let's add up, left, and right to gerald's movement-portfolio.
```
if (userInput.isKeyPressed(KeyEvent.VK_W)){
    setY(getY() - 1);
}
if (userInput.isKeyPressed(KeyEvent.VK_S)){
    setY(getY() + 1);
}
if (userInput.isKeyPressed(KeyEvent.VK_D)){
    setX(getX() + 1);
}
if (userInput.isKeyPressed(KeyEvent.VK_A)){
    setX(getX() - 1);
}
```
here's our code so far:
```java
public class Main {
    public static void main(String[] args){
        Game myGameWindow = new Game(300, 300, "Ciege");
        myGameWindow.showFPS(Game.FPSDisplayMode.TITLE_BAR);
        myGameWindow.show();
        myGameWindow.getSceneManager().switchScene(new Scene(myGameWindow.getSceneManager()){
            AbstractEntity gerald;
            @Override
            public void onCreate() {
                this.gerald = new AbstractEntity(20,20,20,20,getSceneManager()){
                    @Override
                    public void onCreate() {

                    }
                    @Override
                    public void onUpdate() {
                        Input userInput = myGameWindow.getInput();
                        if (userInput.isKeyPressed(KeyEvent.VK_W)){
                            setY(getY() - 1);
                        }
                        if (userInput.isKeyPressed(KeyEvent.VK_S)){
                            setY(getY() + 1);
                        }
                        if (userInput.isKeyPressed(KeyEvent.VK_D)){
                            setX(getX() + 1);
                        }
                        if (userInput.isKeyPressed(KeyEvent.VK_A)){
                            setX(getX() - 1);
                        }
                    }
                    @Override
                    public void onDraw(ScenicGraphics esg) {
                        esg.setColor(Color.blue);
                        esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, false);
                    }
                };
                addEntityToScene(this.gerald);
            }

            @Override
            public void onUpdate() {

            }

            @Override
            public void onDraw(ScenicGraphics sg) {
                sg.setColor(Color.red);
                sg.displayRect(50, 50, 100, 150, true, false);
            }
        });
    }
}
```

###### step 6: camera

So gerald is running around the screen but if we move him long enough, he goes off the screen!
so let's add a camera to the scene and tell the camera to follow gerald.

There are a lot of different cameras available, and we can even create our own!
but for now let's add a default camera to the scene.\
we add a camera by going into our scene and creating a Camera object near where we declare gerald.\
I'll be using "SmoothFollowCamera", but you can also use "FollowCamera".

`SmoothFollowCamera camera;`\
and after we initialize gerald we can initialize the camera:
`this.camera = new SmoothFollowCamera(myGameWindow, this.gerald);`\
and similar to gerald we have to add the camera to the scene:
`setCamera(this.camera);`

if we run the program gerald is always inside the screen, but he's drooping to the bottom left a little bit.
that's because camera's follow the x and y, but don't take width or height into account.

we can fix this by going into gerald's onDraw() method and changing the displayRect method we called.\
let's change it from `esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, false);`\
to `esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, true);`

and if we run the program gerald is now loosely centered in the screen.

![Gerald is centered](https://i.imgur.com/6FRCnI0.png)

###### step 7: Box Colliders

entities can have box colliders which you can detect collisions with.
to add a box collider to your entity first we have to tell the game that this entity has a box collider.
and then we have to set the box collider.

***IMPORTANT!* adding too many colliders to your game may cause your game to run terribly slow.**

so inside gerald's onCreate() method let's execute this line of code.\
`createBoxCollider(new BoxCollider(this, getSceneManager().getCurrentScene()));`

now at the *end* (very important) of your onUpdate() method add this:\
`getBoxCollider().setCollider("MyCollider1", new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()));`

to see the box collider go to the onDraw() method in gerald and add this:
`if (getBoxCollider() != null) getBoxCollider().drawCollider(esg);`

sadly, if you run the program, you'll see that the box collider is not perfectly aligned with his body, and the reason, again, is that we're not taking into account the width and the height of gerald.
so if we want the box collider to be perfectly centered on gerald, we'll have to do it ourselves.

change the `getBoxCollider().setCollider()`'s first parameter to this `(int)(getX()-(getWidth()/2))`\
and then change the second parameter to `(int)(getY() - (getHeight()/2))`

Now if you run the program, you'll see that the green rectangle is on top of gerald.

To see the magic of box colliders let's create a new entity with its own box collider.

So let's make a new entity called obstacle:
```
this.obstacle = new AbstractEntity(100,20,100,20,getSceneManager()){
    @Override
    public void onCreate() {
        createBoxCollider(new BoxCollider(this, getSceneManager().getCurrentScene()));
    }
    @Override
    public void onUpdate() {
        getBoxCollider().setCollider("ObstacleCollider1", new Rectangle((int)(getX()-(getWidth()/2)), (int)(getY() - (getHeight()/2)), (int)getWidth(), (int)getHeight()));
    }
    @Override
    public void onDraw(ScenicGraphics esg) {
        esg.setColor(Color.green);
        esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, true);
        if (getBoxCollider() != null) getBoxCollider().drawCollider(esg);
    }
};
```
also remember to add `addEntityToScene(this.obstacle);`

Going back to gerald's userInput detection section,
let's change the if statements to detect collisions between gerald and any other object.\'
add ` && !getBoxCollider().isColliding()` to the end of every if statement.

When we run the program we can see the other entity, and if we touch it...\
uh oh! gerald is stuck!
the reason this is happening is because the way we are detecting collisions, gerald can never move after he collides on any side.
we can fix this by detecting the individual sides gerald is touching, i.e. gerald is being touched on his left side.

so change the `isColliding()` with `getCollisionSide(Direction.SOUTH)`
but change the south to the side you want to check, so if he is going left, you want to change it to east. if he is going down, you want to change it to south.

###### step 8: Goodbyes

well I had fun, and I hope you did too.
there is a lot more stuff you can do, but this is meant to be a 5-minute tutorial on how to get started and to understand the gist of the engine.

I'm sure gerald had a lot of fun too.\
![Gerald had a ton of fun](https://i.imgur.com/bxdHTd6.png)

here is the source code of what we just did:
```java
public class Main {
    public static void main(String[] args){
        Game myGameWindow = new Game(300, 300, "Ciege");
        myGameWindow.showFPS(Game.FPSDisplayMode.TITLE_BAR);
        myGameWindow.show();
        myGameWindow.getSceneManager().switchScene(new Scene(myGameWindow.getSceneManager()){
            AbstractEntity gerald;
            AbstractEntity obstacle;
            SmoothFollowCamera camera;
            @Override
            public void onCreate() {
                this.gerald = new AbstractEntity(20,20,20,20,getSceneManager()){
                    @Override
                    public void onCreate() {
                        createBoxCollider(new BoxCollider(this, getSceneManager().getCurrentScene()));
                    }
                    @Override
                    public void onUpdate() {
                        Input userInput = myGameWindow.getInput();
                        if (userInput.isKeyPressed(KeyEvent.VK_W) && !getBoxCollider().getCollisionSide(Direction.NORTH)){
                            setY(getY() - 1);
                        }
                        if (userInput.isKeyPressed(KeyEvent.VK_S) && !getBoxCollider().getCollisionSide(Direction.SOUTH)){
                            setY(getY() + 1);
                        }
                        if (userInput.isKeyPressed(KeyEvent.VK_D) && !getBoxCollider().getCollisionSide(Direction.WEST)){
                            setX(getX() + 1);
                        }
                        if (userInput.isKeyPressed(KeyEvent.VK_A) && !getBoxCollider().getCollisionSide(Direction.EAST)){
                            setX(getX() - 1);
                        }
                        getBoxCollider().setCollider("MyCollider1", new Rectangle((int)(getX()-(getWidth()/2)), (int)(getY() - (getHeight()/2)), (int)getWidth(), (int)getHeight()));
                    }
                    @Override
                    public void onDraw(ScenicGraphics esg) {
                        esg.setColor(Color.blue);
                        esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, true);
                        if (getBoxCollider() != null) getBoxCollider().drawCollider(esg);
                    }
                };
                this.obstacle = new AbstractEntity(100,20,100,20,getSceneManager()){
                    @Override
                    public void onCreate() {
                        createBoxCollider(new BoxCollider(this, getSceneManager().getCurrentScene()));
                    }
                    @Override
                    public void onUpdate() {
                        getBoxCollider().setCollider("ObstacleCollider1", new Rectangle((int)(getX()-(getWidth()/2)), (int)(getY() - (getHeight()/2)), (int)getWidth(), (int)getHeight()));
                    }
                    @Override
                    public void onDraw(ScenicGraphics esg) {
                        esg.setColor(Color.green);
                        esg.displayRect(getX(), getY(), getWidth(), getHeight(), true, true);
                        if (getBoxCollider() != null) getBoxCollider().drawCollider(esg);
                    }
                };
                this.camera = new SmoothFollowCamera(myGameWindow, this.gerald);
                setCamera(this.camera);
                addEntityToScene(this.gerald);
                addEntityToScene(this.obstacle);
            }

            @Override
            public void onUpdate() {

            }

            @Override
            public void onDraw(ScenicGraphics sg) {
                sg.setColor(Color.red);
                sg.displayRect(50, 50, 100, 150, true, false);
            }
        });
    }
}
```