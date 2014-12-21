<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="incidentApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AngularJS Incident</title>
<script data-require="angular.js@*" data-semver="1.2.13" src="http://code.angularjs.org/1.2.13/angular.js"></script>
<script type="text/javascript" src="./js/app.js"></script>
</head>
<body>
	<div ng-controller="incidentController">
		<div id="add-incident-panel">
			<div>
				<span>Add Incident</span>
			</div>
			<div>
				<div>
					<table>
						<tr>
							<td>Customer ID:</td>
							<td><input type="text" ng-model="customerId" /></td>
						</tr>
						<tr>
							<td>Customer Address:</td>
							<td><input type="text" ng-model="customerAddress" /></td>
						</tr>
						<tr>
							<td>
								<button ng-click="addIncident()" class="btn-panel-big">Add New Incident</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>