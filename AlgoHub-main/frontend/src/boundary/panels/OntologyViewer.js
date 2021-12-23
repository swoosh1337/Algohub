import React, { useState, useReducer } from 'react';
import Tree from '../../lib/tree-view/index';
import { useSelector, useDispatch } from 'react-redux';
import {selectOntologyItem, toggleOntologyItem, updateRemoveRequest} from "../../model/ViewModel";
import store from '../../model/ModelProxy';

const treeStyles = {

    color: 'black',
    fill: 'black',
    fontSize: "10pt",
    width: '100%',
    marginLeft: "15px"
  }


//Credit for recursion: https://betterprogramming.pub/recursive-rendering-with-react-components-10fa07c45456 

function getOntology(ontologyData) {

    if(ontologyData) {

        return ontologyData.map((item) => ({
            ...item,
            hasChildren: ontologyData.filter((i) => i.parentId === item.id).length > 0,
        }));
    } else {

        return []
    }

}


function TreeWrapper({ onRemove, toggleExpanded, enableRemove, expandedOntologyItems, onSelect, selected, treeData, parentId = 0, level = 0 }) {

    const items = treeData
    .filter((item) => item.parentId == parentId)
    .sort((a, b) => (a.name > b.name ? 1 : -1));


    if (!items.length) return null;

    return (
        <>
            
            {items.map((item, idx) => {


                var style = treeStyles;

                var key = level + item.parentId + "." + item.id;
                
                if(item.hasChildren) {

                    return <Tree 
                                selected={selected} 
                                enableRemove={enableRemove}
                                content={item.name} 
                                style={{...style, color:"#ac2b37"}} 
                                key={key}
                                item={item}
                                onSelect={(i) => onSelect(i)}
                                expandedOntologyItems={expandedOntologyItems} 
                                toggleExpanded={toggleExpanded}
                                onRemove={onRemove}
                            >
                                <TreeWrapper 
                                                enableRemove={enableRemove}
                                                expandedOntologyItems={expandedOntologyItems} 
                                                onSelect={(i) => onSelect(i)} 
                                                selected={selected} 
                                                treeData={treeData} 
                                                parentId={item.id} 
                                                level={level + 1} 
                                                toggleExpanded={toggleExpanded}
                                                onRemove={onRemove}
                                />
                        </Tree>

                } else {
                    
                    return <Tree 
                                    enableRemove={enableRemove}
                                    onSelect={(i) => onSelect(i)} 
                                    item={item}
                                    selected={selected} 
                                    content={item.name} 
                                    style={style} 
                                    key={key}
                                    expandedOntologyItems={expandedOntologyItems} 
                                    toggleExpanded={toggleExpanded}
                                    onRemove={onRemove}
                            />
                }
            })}
        
        </>
    );
}

export default function OntologyViewer(props) {

    const ontologyHierarchy = useSelector(state =>  state.model.ontologyHierarchy);
    var expandedOntologyItems = useSelector(state =>  state.viewModel.expandedOntologyItems);
    const selectedOntologyItem = useSelector(state => state.viewModel.selectedOntologyItem);

    const dispatch = useDispatch();

    var expandItem = (id) => {

        dispatch(toggleOntologyItem(id));
    }

    return (
        <div>
            <TreeWrapper 
                expandedOntologyItems={expandedOntologyItems} 
                onSelect={(item) => props.onSelect(item)} 
                enableRemove={props.enableRemove}
                selected={selectedOntologyItem} 
                treeData={getOntology(ontologyHierarchy)}
                toggleExpanded={expandItem}
                onRemove={props.onRemove}
            />
        </div>
    );
}
