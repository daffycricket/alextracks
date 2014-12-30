
angular
	.module('AlexTracksApp', ['ngRoute', 'google-maps'])
	.config(function($routeProvider) {
		
		// configure routes
		$routeProvider
		
			// list incidents
			.when('/', {
				templateUrl: 'views/list.html',
				controller: 'MainController'
			})
			
			// list incidents on map
			.when('/map', {
				templateUrl: 'views/map.html',
				controller: 'MapController'
			});
	})
	.controller('MainController', function($scope, $http) {
		$scope.title = 'Follow my incidents !'
		$http.get('http://localhost:8080/alextracks/api/rest/incidents')
		.success(function (data, status, headers, config) {
			$scope.incidents = data;
			$scope.is_backend_ready = true;
			$scope.$apply();
		})
		.error(function (data, status, headers, config) {
			alert("Aouch: Status " + status + " - " + data);
		});
	})
	.controller('MapController', function($scope, $http) {
		$scope.title = 'Follow my incidents on a Map!'
		$http.get('http://localhost:8080/alextracks/api/rest/incidents').
		success(function (data, status, headers, config) {
			
			var markers = new Array(data.length);
			for(i = 0; i < data.length; i++){
				markers[i] = {
					latitude: data[i].location.latitude, 
					longitude: data[i].location.longitude,
					creationTs: data[i].creationTs,
					formattedAddress: data[i].formattedAddress,
					customerId: data[i].customerId
				};
			}
			
			$scope.map = {
				zoom: 12,
				showTraffic: false,
				showWeather: false,
				stroke: { weight: 2, color: "#6060FB" },
				incidents: markers
			};
			
			//center is Eiffel Tower
			$scope.map.center = { 
				latitude: 48.858346906910576, 
				longitude: 2.2945250598645472
			};
		}).
		error(function (data, status, headers, config) {
			alert("Aouch: Status " + status + " - " + data);
		});
	});