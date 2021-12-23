
export function expandParents(expandedOntologyItems, ontology, hierarchyElement) {

    expandedOntologyItems[hierarchyElement.id] = true

    var parent = ontology.filter((item) => {
        return item.id == hierarchyElement.parentId;
    });

    if (parent.length == 1) {

        expandParents(expandedOntologyItems, ontology, parent[0])

    } else {

        expandedOntologyItems[hierarchyElement.id] = true
    }
}

export function powerOfTwo(x) {
    return (Math.log(x)/Math.log(2)) % 1 === 0;
}

export function validateNum(vals, errors, name) {

    if(name in vals) {

        var val = vals[name]
        var err = "";
        err = val ? "" : "This field is required. ";
    
        var num = parseInt(vals[name]);
        if(isNaN(num)) {
    
            err = "Size must be a number";
    
        }
    
        errors[name] = err
    } 

}

export function validateStr(vals, errors, name, constrainedLength = false) {

    if(name in vals) {

        var val = vals[name]
        var err = "";
        err = val ? "" : "This field is required. ";
    
        if(constrainedLength && val.length > constrainedLength) {
    
            err = "Field must not be greater then " + constrainedLength;
        }
    
        errors[name] = err

    }
}
