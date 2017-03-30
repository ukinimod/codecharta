"use strict";
/**
 * Controls the treeView
 */
class TreeViewController {

    /* @ngInject */

    /**
     * @constructor
     * @param {Scope} $scope
     * @param {DataService} dataService
     * @param {CodeMapService} codeMapService
     * @listens {data-changed}
     */
    constructor($scope, dataService, codeMapService) {

        this.tree = dataService.data.currentmap;

        let ctx = this;
        $scope.$on("data-changed", (e,d)=>{ctx.onDataChanged(d);});


    }

    onDataChanged(dataModel) {
        this.tree = dataModel.currentmap;
        console.log(this.tree);
    }

    onTreeToggle(ivhNode, ivhIsExpanded, ivhTree) {
        console.log("toggle");
    }

    onTreeCheckboxChanged(ivhNode, ivhIsExpanded, ivhTree) {
        console.log("select/deselect");
    }


}

export {TreeViewController};


