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

        this.treeOptions = {
            twistieCollapsedTpl: "<i class='fa fa-folder fa-lg'></i>",
            twistieExpandedTpl: "<i class='fa fa-folder-open fa-lg'></i>",
            twistieLeafTpl: "<i class='fa fa-file fa-lg'></i>",
            nodeTpl: "<tree-view-node-directive></tree-view-node-directive>"
        };


    }

    onTreeToggle(ivhNode, ivhIsExpanded, ivhTree) {
        console.log("toggle", ivhNode);
    }

    onTreeCheckboxChanged(ivhNode, ivhIsExpanded, ivhTree) {
        console.log("select/deselect", ivhNode);
    }


}

export {TreeViewController};


