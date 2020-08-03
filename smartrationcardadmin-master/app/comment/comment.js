'use strict';

angular.module('webApp.comment', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/review',{
		templateUrl: 'comment/comment.html',
		controller: 'CommentCtrl'
	});
}])

.controller('CommentCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location' , 'distributeridservice' , function($scope, CommonProp, $firebaseArray, $firebaseObject, $location ,distributeridservice ){
	$scope.username = CommonProp.getUser();
	if(!$scope.username){
		$location.path('/home');
    }
    

        
    var ref = firebase.database().ref().child('Reviews');
     $scope.allreview = $firebaseArray(ref);	

        //     $scope.allreview = {};
        //     ref.on('value', function(items) {  
        //      $scope.$apply(function () {
        //         $scope.allreview = items;
        //         console.log("AllReview" + $scope.allreview.length );
        // });
        // });

    // $scope.allreview.$loaded().then(function(distributers) {
    

    // });

//     $scope.newComments = {};
// ref.once('value' ,function(snapshot){
// snapshot.forEach(function(childshnap){
//     childshnap.forEach(function(comment)
//     {
//         var trips = [];
//         id = comment.key;
//         val = comment.val();
//         $scope.newComments = trips;
//     })
// })

// })

//    ref.once('value') 
//         .then(reviewSnap => {

//             var trips = [];
//             reviewSnap.forEach((trip) => {
//      trips.push({
//     id: trip.key,
//   });
// });
// $scope.reviewdata = trips;
//     });
    


//     $scope.getAllreview = function()
//     {  
//         $scope.allreview = $firebaseArray(ref);	
        
//         ref.once('value') 
//     .then(reviewSnap => {

//         var trips = [];
//         reviewSnap.forEach((trip) => {
//  trips.push({
// id: trip.key,
// });
// });
// $scope.reviewdata = trips;
// console.log("AllReview" + $scope.allreview.length );

// });

    // }
    
    // console.log("All Review Length" +$scope.allreview.length );

    // $scope.getAllComment = function(id)
    // {
    //     var allcommentref = firebase.database().ref().child('DistributorReviews').child(id);
    //     $scope.allcomment = $firebaseArray(allcommentref);	
        
      
    //     $scope.allcomment = {};
    //     allcommentref.on('value', function(items) {  
    //      $scope.$apply(function () {
    //         $scope.allcomment = items;
    // });
    // });

    //     $scope.allcomment.$loaded().then(function(allcomment) {

    //         $scope.allcomment = $firebaseArray(allcommentref);
    //         console.log("All comment Length" +allcomment.length );
    //         return $scope.allcomment;

    //          });
    
    //     };






}])