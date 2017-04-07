"use strict";

class TreeViewCheckboxDirective {

    /**
     * @constructor
     */
    constructor(ivhTreeviewMgr) {

        this.mgr = ivhTreeviewMgr;

        this.require="^ivhTreeview";

        this.restrict = "AE";

        this.template = [
            "<span class='ascii-box'>[",
            "<span ng-show='node.selected'>x</span>",
            "<span ng-show='node.__ivhTreeviewIndeterminate'>~</span>",
            "<span ng-hide='node.selected || node.__ivhTreeviewIndeterminate'> </span>",
            "]</span>",
            "<span class='ivh-treeview-node-label' ivh-treeview-toggle>",
            "{{trvw.label(node)}}",
            "</span>"
        ].join("");

    }

    link(scope, element, attrs) {
        let ivhTreeviewMgr = this.mgr;
        element.on("click", function() {
            scope.$apply(ivhTreeviewMgr.select(scope.trvw.root(), scope.node, !scope.node.selected));
        });
    }
    
}

export {TreeViewCheckboxDirective};
