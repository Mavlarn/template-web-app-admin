'use strict';

angular
    .module('firstHouseAdmin')
    .controller('DashboardController', DashboardController);

function DashboardController($scope,$rootScope,$interval) {

    var timer = $interval(function(){
        $scope.currentTime = moment().format('YYYY年M月D日 HH:mm:ss');
    }, 1000); 
    $scope.chart = {
      count1: 231,
      count2:2421
    };
    $scope.chart.options = {
      'chart': {
        'type': 'multiBarChart',
        'height': 450,
        'margin': {
          'top': 20,
          'right': 20,
          'bottom': 60,
          'left': 45
        },
        'legend': {
            'padding': 30
        },
        'showControls': false,
        'clipEdge': true,
        'staggerLabels': true,
        'transitionDuration': 500,
        'stacked': true,
        'xAxis': {
          'axisLabel': '日期',
          'showMaxMin': false,
            'tickFormat': function(d) {
                return d3.time.format('%x')(new Date(d));
            }
        },
        'yAxis': {
          'axisLabel': '用户数量',
          'axisLabelDistance': 40
        }
      }     
    };
	  $scope.Get_Greetings = function() {  
       var now = new Date();  
       var times = now.getHours();  
       var whe=parseInt(times);  
       if(times>=0 && times<6){return "凌晨"}  
       if(times>=6 && times<9){return "早上"}  
       if(times>=9 && times<11){return "上午"}  
       if(times>=11 && times<13){return "中午"}  
       if(times>=13 && times<17){return "下午"}  
       if(times>=17 && times<19){return "傍晚"}  
       if(times>=19 && times<24){return "晚上"}  
    };   

    $scope.chart.data = [{
        key: '转化数',
        values: [
          {x: new Date(2015, 1, 1), y: 17},
          {x: new Date(2015, 1, 2), y: 6},
          {x: new Date(2015, 1, 3), y: 14},
          {x: new Date(2015, 1, 4), y: 8},
          {x: new Date(2015, 1, 5), y: 9},
          {x: new Date(2015, 1, 6), y: 17},
          {x: new Date(2015, 1, 7), y: 5},
          {x: new Date(2015, 1, 8), y: 14},
          {x: new Date(2015, 1, 9), y: 7},
          {x: new Date(2015, 1, 10), y: 18},
          {x: new Date(2015, 1, 11), y: 9},
          {x: new Date(2015, 1, 12), y: 6},
          {x: new Date(2015, 1, 13), y: 24},
          {x: new Date(2015, 1, 14), y: 5},
          {x: new Date(2015, 1, 15), y: 11},
          {x: new Date(2015, 1, 16), y: 8},
          {x: new Date(2015, 1, 17), y: 28},
          {x: new Date(2015, 1, 18), y: 11},
          {x: new Date(2015, 1, 19), y: 11},
          {x: new Date(2015, 1, 20), y: 16},
          {x: new Date(2015, 1, 21), y: 6},
          {x: new Date(2015, 1, 22), y: 8},
          {x: new Date(2015, 1, 23), y: 11},
          {x: new Date(2015, 1, 24), y: 13},
          {x: new Date(2015, 1, 25), y: 7},
          {x: new Date(2015, 1, 26), y: 19},
          {x: new Date(2015, 1, 27), y: 9},
          {x: new Date(2015, 1, 28), y: 18},
          {x: new Date(2015, 1, 29), y: 5},
          {x: new Date(2015, 1, 30), y: 8},
          {x: new Date(2015, 1, 31), y: 25}]
      }, {
        key: '用户数',
        values: [
          {x: new Date(2015, 1, 1), y: 800},
          {x: new Date(2015, 1, 2), y: 500},
          {x: new Date(2015, 1, 3), y: 600},
          {x: new Date(2015, 1, 4), y: 700},
          {x: new Date(2015, 1, 5), y: 500},
          {x: new Date(2015, 1, 6), y: 456},
          {x: new Date(2015, 1, 7), y: 800},
          {x: new Date(2015, 1, 8), y: 589},
          {x: new Date(2015, 1, 9), y: 467},
          {x: new Date(2015, 1, 10), y: 876},
          {x: new Date(2015, 1, 11), y: 689},
          {x: new Date(2015, 1, 12), y: 700},
          {x: new Date(2015, 1, 13), y: 500},
          {x: new Date(2015, 1, 14), y: 600},
          {x: new Date(2015, 1, 15), y: 700},
          {x: new Date(2015, 1, 16), y: 786},
          {x: new Date(2015, 1, 17), y: 345},
          {x: new Date(2015, 1, 18), y: 888},
          {x: new Date(2015, 1, 19), y: 888},
          {x: new Date(2015, 1, 20), y: 888},
          {x: new Date(2015, 1, 21), y: 987},
          {x: new Date(2015, 1, 22), y: 444},
          {x: new Date(2015, 1, 23), y: 999},
          {x: new Date(2015, 1, 24), y: 567},
          {x: new Date(2015, 1, 25), y: 786},
          {x: new Date(2015, 1, 26), y: 666},
          {x: new Date(2015, 1, 27), y: 888},
          {x: new Date(2015, 1, 28), y: 900},
          {x: new Date(2015, 1, 29), y: 178},
          {x: new Date(2015, 1, 30), y: 555},
          {x: new Date(2015, 1, 31), y: 993}
      ]}
    ];

}

angular
    .module('firstHouseAdmin')
    .controller('DashboardController', DashboardController);
