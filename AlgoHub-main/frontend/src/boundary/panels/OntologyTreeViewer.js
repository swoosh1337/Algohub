import React, { useState, useCallback } from "react";
import ReactDOM from "react-dom";
import Graph from "react-graph-vis";
import debounce from 'lodash.debounce';
import 'vis-network/dist/dist/vis-network.min.css';
import { toPlainObject } from "lodash";

import { useSelector, useDispatch } from 'react-redux'

export default function OntologyTreeViewer(props) {

    const [network, setNetwork] = useState(null)
    const [graph, setGraph] = useState({
        nodes: [],
        edges: []
    });


    const options = {
        interaction: { hover: true },
        manipulation: {
            initiallyActive: true,
            deleteNode: (props.enableRemove) ? function(node, cb) {
                
                handleNodeDelete(node, cb)
            } : false,
            deleteEdge: false,
            editEdge: false,
            addNode: false,
            addEdge: false
        },
        autoResize: true,
        physics: {
            enabled: false
        },
        layout: {
            hierarchical: {
                "direction": "UD",
                "sortMethod": "directed",
                "nodeSpacing": 300,
                "shakeTowards": "roots"
            }
        },
        edges: {
            color: "#000000"
        },
        height: "100%"
    };

    const handleNodeDelete = (items, cb) => {

        var nodes = items.nodes;
        if(nodes.length > 0) {
            var clickedNode = network.body.nodes[nodes[0]].options.actualNode;
            console.log("node to remove: ",clickedNode)
            if(clickedNode) {
                props.onRemove(clickedNode);
            }
        }

        cb([])
    }

    const debouncedFit = debounce((e) => {

        if(!network) {

            setTimeout(() => debouncedFit(), 500)

            return
        }
        fitNetwork()

    }, 10)

    const getColor = (type)=> {

        if(type=="algorithm") {
            return "#43A047"
        }

        if(type=="classification") {

            return "#ac2b37"
        }

        if(type=="implementation") {
            return "#2196F3"
            
        }
    }

    const getShape = (type)=> {

        if(type=="algorithm") {
            return "box"
        }

        if(type=="classification") {

            return "box"
        }

        if(type=="implementation") {
            return "circle"
            
        }
    }
    const selected = useSelector(state => state.viewModel.selectedOntologyItem);

    var fitNetwork = () => {

        if(network) {
            if(selected && selected.id) {

                network.setSelection({
                    nodes: [
                        selected.id
                    ]
                })
            }
            network.fit()
            network.moveTo({ offset: { y: - (0.5 * (network.body.container.clientHeight /  3)) } })
        }

    }

    React.useEffect(() => {
        fitNetwork()
    }, [network]);

    const ontologyHierarchy = useSelector(state => state.model.ontologyHierarchy);

    React.useEffect(() => {

        if(!ontologyHierarchy) {
            return;
        }

        var nodes = ontologyHierarchy.map((item) => {
            return {
                label: item.name,
                id: item.id,
                color: getColor(item.typeName),
                font: {
                    color: "white"  
                },
                shape: getShape(item.typeName),
                actualNode: item
            }
        });

        var edges = []

        for(var i in ontologyHierarchy) {

            var item = ontologyHierarchy[i];
            var parent = ontologyHierarchy.filter((p) => p.id == item.parentId);
            console.log(parent)
            if(parent.length > 0) {

                edges.push({
                    from: parent[0].id,
                    to: item.id
                })

            } else {

                edges.push({
                    from: "visjs_root_node",
                    to: item.id
                })
            }
        }

        nodes.push({
                    label: "root", 
                    id: "visjs_root_node",
                    color: getColor("classification"),
                    font: {
                        color: "white"  
                    },
                    shape: getShape("classification")
                })


        setGraph(
            {
                nodes: nodes,
                edges: edges
            }
        )

        debouncedFit()
    }, [ontologyHierarchy ?? []]);

    const events = {
        select: function (event) {
            var { nodes, edges } = event;
            if(nodes.length > 0) {

                var clickedNode = this.body.nodes[nodes[0]];
                if(clickedNode.options.actualNode) {

                    console.log(clickedNode)
                    props.onSelect(clickedNode.options.actualNode);
                }
            }
        },
        resize: function (event) {
            debouncedFit(event)
        },
        deleteNode: function(event) {

            
        }
    };
    return (
        <Graph
            graph={graph}
            options={options}
            events={events}
            getNetwork={network => {

                setNetwork(network)
            }}
        />

    );
}
