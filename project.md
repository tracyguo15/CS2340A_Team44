# Introduction

GreenPlate is an app designed to help reduce the waste of food, save people money by helping them utilize all of the food that they buy, and help people track their daily calorie intake. Greenplate is dedicated to aiding people in their everyday food management plans. To help create more sustainable habits, the app allows users to be able to digitally track food items in their pantry, generate recipes to help ensure that all items in the pantry get eaten, and create shopping lists so that a user does not accidentally purchase food items that already are in their pantry. By shopping and planning meals with sustainability in mind, users can save money and more affordably feed themselves and their families. In addition to eating and shopping sustainability, the GreenPlate app also provides users with information on their calorie intake. Users can use this knowledge to help them reach their eating goals and establish healthy eating habits. GreenPlate’s overarching purpose is to increase food efficiency one pantry at a time.


# Design and Architecture

## Use Case Diagram

Here we have the use case diagram for our first sprint where we were defining how primary and supporting actors would interact with the app, and who could potentially be supporting the development and production of the app. The user being a primary actor in this diagram interacts with the app by logging into the app, entering in their calorie intake to be recorded, adding items to their shopping list, and selecting a recipe to cook. Understanding how the different actors such as the user interact with the system allowed the development of the GreenPlate app to go smoothly by providing a consistent description of what needed to be implemented down the road. 

<img width="620" alt="Use Case" src="UseCase.png">

## Domain Model
This is the Domain model that we developed early on in the project to be able to define the different classes and their different attributes. This domain model was created in order to bridge the written description of what GreenPlate was going to be to something easy to understand when generating software. The domain model was useful as well to help understand how the different classes will interact with one another such as how classes will interact with an interface.

<img width="620" alt="Domain Model" src="DomainModel.png">

## MVVM Architecture
In our project we decided to use MVVM as our architecture. This is mainly because it was the preferred development pattern for Android mobile development. In our model, we have around 9 classes representing important concepts in our system. Namely, Ingredients, Recipes, and Meals, which represent the food; a Pantry which stores the food; a RecipeBook and Health Report to keep track of meals eaten in the past and future. There is also a Trash Bin that keeps track of food wasted. This is the internal workings of our system, however, we need to keep our system responsive to the user, and let the user update the system while the system notifies the user of what actions they can take. We created Model classes (located in the modelfolder) to keep track of any internal classes used by the system. We then made ViewModels (kept in the viewmodels folder)  which would be an intermediate layer for communicating between the internal system models and the user inputs. Finally, we have Views (located in the views folder), which represent the visual GUI that the user will interact with. The Views folder holds the classes which implement the logic for making GUI components work. Along with the Views folder is the res/layout folder, which lays out all the components of the GUI that the app will use.


<img width="288" alt="MVVM" src="MVVM.png">
<img width="288" alt="MVVM" src="MVVM2.png">

## Updated Domain Model
This is the updated Domain model that we created in the second sprint of the project to be able to further define the relationships between the classes we already had, and the new ones that we added. The updated model allowed for a better understanding of how the new classes will be included within the scope of our current implementation, and what attributes that they contain.

<img width="641" alt="Updated Domain Model" src="UpdatedDomainModel.png">

## System Sequence Diagram and Sequence Diagram
Springboarding off of the Use Case Diagram, our team created an initial system sequence diagram and a sequence diagram. The diagrams below were developed during sprint two to get a better idea of what a user would specifically have to do in order to accomplish a specific task on the app.

### Use Case Scenarios
<img width="607" alt="UseCaseScenarios1" src="UseCaseScenarios1.png">

### System Sequence Diagram
<img width="429" alt="SSD" src="SSD.png">

### Sequence Diagram
<img width="613" alt="SD" src="SD.png">


## Singleton Pattern Evidence
The code presented in the image above follows the singleton pattern by creating only one instance. The singleton design pattern requires there to be only one private instance of a variable and globally accessible through one pathway. The variable titled “uniqueInstance” is the only instance created. The variable is private to help ensure that no external class is able to gain access and in turn generate another instance of the singleton class. Finally, the only way to access the variable is through the “getInstance()” method. This method communicates with the firebase authentication database to retrieve information about usernames(emails) and passwords using the variable. There is only one instance that can actually communicate with the firebase authentication and for other classes such as the createAccountViewModel, it would have to call that one instance to send and retrieve appropriate data to firebase. As a result, by utilizing a singleton pattern, it allows the class surrounding the firebase to execute more efficiently and utilize resources effectively due to only one instance to be created.  

<img width="466" alt="singleton" src="singleton.png">

## Design Class Diagram

For our third sprint we developed a Design Class Diagram to further define the different classes from our previous domain model. We were also able to learn more about how additional classes needed to interact with what was already established. 

<img width="710" alt="DCD" src="DCD.png">


## Updated Sequence Diagram and Use Case Scenarios
Here is another sequence diagram developed during our third sprint that included the functionality we later implemented for the sprint. 
### Use Case Scenarios
<img width="612" alt="SD3" src="SD3.png">

### Sequence Diagram
<img width="710" alt="UseCase3" src="UseCase3.png">

## Design Principles Examples: SOLID

### Liskov Substitution Principle
<img width="460" alt="LSP1" src="LSP1.png">

<img width="473" alt="LSP2" src="LSP2.png">
The top image is the parent class AbstractDatabase with one of its children being the PantryData class. The Liskov substitution principle covers how parent and child classes impact one another. Child classes are supposed to be able to substitute for their parent classes without it being noticeable. The PantryData class has many of the methods that its parent class contains and is able to complete the same functionality such as adding items and deleting items similar to how the AbstractDatabase completed the put and remove method which is in line with the Liskov substitution principle.

### Interface Segregation Principle
<img width="553" alt="ISP" src="ISP.png">

The interface segregation principle focuses on not using large interfaces and utilizing small focused interfaces. The IngredientCallback class is an interface that focuses on ensuring that we are properly updating the app’s interface. The IngredientCallback class is not a multipurpose interface. It just focuses on one main purpose of ensuring that the code implemented effectively interacts with the interface of the app with one singular method to implement in a concrete class. This allows for the classes that utilize the interface to not be reliant on one another and able to complete their tasks separately.

### Dependency Inversion Principle

<img width="495" alt="DIP" src="DIP.png">

The dependency inversion principle highlights that classes should depend on interfaces and abstract methods instead of concrete implementations in another class, and that high level modules should not be tightly coupled with lower level modules. In the example above the top image is the interface, and the bottom is a screenshot of the addToFirebase method in another class that utilizes the interface. The class that contains addToFirebase is following the dependency inversion principle by utilizing an interface and an abstract method in order to complete the implementation instead of relying on a concrete class. The interface class IngredientCallback is abiding by the dependency inversion principle by not relying on details from lower level modules.


## Design Principles Examples: GRASP

### Information Expert

<img width="534" alt="IE" src="IE.png">

The information expert principle of GRASP emphasizes classes holding necessary data for its operations, therefore being an expert on its data by encapsulating the data needed to create its functions. The IngredientData.java class is an example that complies with this principle because it encapsulates all relevant data about an ingredient, such as its name, calories, price, quantity, and expiration date. This encapsulation allows IngredientData to have the necessary information to perform operations related to an ingredient, making it an expert on that data.

### Controller
<img width="312" alt="Controller" src="Controller.png">

The controller principle is a pattern where all non-UI logic is passed down to a base model. In this case, PantryPageViewModel class acts as the controller class, by making it so that the user has access to their pantry, and can add, modify, and remove items at will. It does this by being instantiated when the UI class is created, and depending on which button is pressed, updating the PantryData database. This makes it so that it is easier to separate UI logic from the backend code.

### Creator

<img width="635" alt="Creator" src="Creator.png">

The creator principle of GRASP determines which programs are responsible for creating an object, or a new instance of another class. The goal is to avoid creating redundant objects and to only create one if necessary, such as if one class has initializing data or closely uses another class. In this example that’s in the IngredientViewModel, it complies with this principle as the IngreidentViewModel is creating a DataForPantry object, This is because the viewmodel has all of the initializing data for DataForPanty, and uses the object closely as the object is what’s added to firebase. 


## Design Patterns
### Strategy Pattern
<img width="537" alt="SP1" src="SP1.png">

<img width="392" alt="SP2" src="SP2.png">

<img width="377" alt="SP3" src="SP3.png">


The design strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. The strategy lets the algorithm vary independently from clients that use it. The classes IngredientCallback, IngredientViewModel, and IngredientPage reflect this pattern. The IngredientCallback Interface defines a contract for the strategies, encapsulating the different possible completion actions for an operation with ingredients as the onCompleted(int result) method. It serves as an abstraction for the algorithm that can be called upon completion of the background task. Next, the IngredientViewModel class plays the role of the context in the strategy pattern. It maintains a reference to a IngredientCallback strategy and communicates with it via the addToFirebase method. By accepting a IngredientCallback as a parameter, IngredientViewModel is designed to work with any strategy that adheres to the IngredientCallback interface. This setup allows the IngredientViewModel to delegate the post-operation action, depending on the outcome of adding ingredients to the database, to the concrete strategy provided. Finally, the IngredientPage class acts as a client of the strategy pattern. It holds the UI logic and interacts with the IngredientViewModel to initiate actions. When it's time to report the outcome of an action, such as adding an ingredient, IngredientPage supplies a concrete implementation of the IngredientCallback strategy to IngredientViewModel. It defines the specific behavior of the algorithm to execute when the action is completed, be it success, failure, or duplication, based on the user interaction. By separating the concerns, the strategy pattern is effectively used to manage different outcomes of adding an ingredient, without coupling the outcome handling to the IngredientViewModel context. This enables easier maintenance and expansion for handling other types of operations that might be introduced in the future.

### Adapter Pattern

<img width="489" alt="AP" src="AP.png">
The adapter pattern is primarily utilized to allow items with incompatible interfaces to be able to work together in order to achieve some goal. The adapter pattern falls under the structural pattern’ umbrella' so to speak. Structural patterns unite different classes and objects into a bigger structure without affecting the efficiency.  Here in the code below we have implemented the adapter pattern by creating an adapter class that can be utilized to make an adapter object. Our adapter in this instance is allowing us to be able to adapt a list into the list view. By implementing the adapter pattern we were able to unite the list and the list view which have separate interfaces in order to have them be compatible together. This in turn will benefit the application’s implementation later down the road by connecting separate classes to improve the code’s capabilities without impacting the functionality.

### Observer Pattern
The observer pattern is used to be able to notify other objects about events that happen to other objects that are relevant to their use. This allows classes with dynamic data to not have to be altered when introducing another class that relies on that data for functionality. When you implement the observer pattern, it connects objects together at runtime to reduce errors caused by miscommunication between classes. In our code, the getDatabases method calls methods that implement the observer pattern to update data. The method onDataChange implements the observer pattern and updates the data through listeners. The observer pattern allows for our pantry and cookbook database to be able to stay updated to ensure no errors when a user is at the store getting food, or is trying to select a recipe.

<img width="489" alt="OP1" src="OP1.png">

<img width="489" alt="OP2" src="OP2.png">

<img width="489" alt="OP3" src="OP3.png">

# User Interface

# Functionality
[Watch Video Demo](https://drive.google.com/file/d/1l6r8kNe42IKGBu-0dZnQlR6O4NSW1O9C/view?usp=sharing)

# Conclusion







