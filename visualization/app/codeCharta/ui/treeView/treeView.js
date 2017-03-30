"use strict";

import {TreeViewDirective} from "./treeViewDirective.js";

angular.module(
    "app.codeCharta.ui.treeView",
    ["ivh.treeview"]
);

angular.module("app.codeCharta.ui.treeView").directive(
    "treeViewDirective",
    () => new TreeViewDirective()
);

