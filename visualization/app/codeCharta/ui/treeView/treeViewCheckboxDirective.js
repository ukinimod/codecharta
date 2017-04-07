"use strict";

class TreeViewCheckboxDirective {

    /**
     * @constructor
     */
    constructor(ivhTreeviewMgr, settingsService) {

        this.mgr = ivhTreeviewMgr;
        this.settingsService = settingsService;

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
        let ctx = this;
        element.on("click", function() {
            scope.$apply(()=>{
                ivhTreeviewMgr.select(scope.trvw.root(), scope.node, !scope.node.selected);
                ctx.settingsService.onSettingsChanged();
                //the treemapService transforms the node and maps the values to node.selectedInTreeview and sets undefined to true since the default value is true
            });
        });
    }
    
}

export {TreeViewCheckboxDirective};

