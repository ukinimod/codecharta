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
     * @listens {data-changed}
     */
    constructor($scope, dataService) {

        this.tree = dataService.data.currentmap;

        let ctx = this;
        $scope.$on("data-changed", (e,d)=>{ctx.onDataChanged(d);});

        this.treeOptions = {
            labelAttribute: "name",
            childrenAttribute: "children",
            useCheckboxes: true,
            defaultSelectedState: true,
            expandToDepth: -1,
            validate: true,
            twistieCollapsedTpl: "<i class='fa fa-folder fa-lg'></i>",
            twistieExpandedTpl: "<i class='fa fa-folder-open fa-lg'></i>",
            twistieLeafTpl: "<i class='fa fa-file fa-lg'></i>"
        };

    }

}

export {TreeViewController};


