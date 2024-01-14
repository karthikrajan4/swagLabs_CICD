Feature: To check the full flow functionality of SwagLabs order placement

@swagLabFullFlow 
Scenario Outline: <ScenarioID>

	Given User launches the browser with "swagLabs_URL" website
	And User login into the swagLabs Portal
	Then User proceeding with an order
	
	Examples:
		|ScenarioID|
		|SC01|