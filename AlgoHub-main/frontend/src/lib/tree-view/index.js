

import React from 'react'
import PropTypes from 'prop-types'
import { Spring, config, animated } from 'react-spring'
import styled from 'styled-components';
import * as Icons from './icons'
import IconButton from '@mui/material/IconButton';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';


const styles = {
  tree: {
    position: 'relative',
    padding: '4px 0px 0px 0px',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    verticalAlign: 'middle',
  },
  toggle: {
    width: '1em',
    height: '1em',
    marginRight: 10,
    cursor: 'pointer',
    verticalAlign: 'middle',
  },
  type: {
    textTransform: 'uppercase',
    fontFamily: 'monospace',
    fontSize: '0.6em',
    verticalAlign: 'middle',
  },
  contents: {
    willChange: 'transform, opacity, height',
    marginLeft: 6,
    marginTop: 4,
    padding: '0px 0px 0px 10px',
    borderLeft: '1px dashed black'
  },
}


const ContentWrapper = styled.span`

    display: inline-block;
    background-color: ${props => props.selected ? "lightgray" : "white"};
    vertical-align: 'middle';
    border-radius: 5px;
    padding: 5px;
    margin-right: 10px;
    border: 1px solid #ccc;

`;

const Placeholder = styled.div`


`;

export default class Tree extends React.PureComponent {

  constructor(props) {
    super(props)
    this.state = { visible: props.visible, immediate: false }
  }

  toggle = () => {

    var result = (this.props.children && this.setState(state => ({ immediate: false })))

    this.props.toggleExpanded(this.props.item.id);

    this.forceUpdate();
  }

  toggleVisibility = () => {
    this.setState(
      state => ({ visible: !state.visible, immediate: true }),
      () => this.props.onClick && this.props.onClick(this.state.visible)
    )
  }

  componentWillReceiveProps(props) {

      this.setState(state => {
        return ['open', 'visible'].reduce(
          (acc, val) =>
            this.props[val] !== props[val] ? { ...acc, [val]: props[val] } : acc,
          {}
        )
      })


  }

  render() {
    const {  visible, immediate } = this.state
    const { onRemove, enableRemove, expandedOntologyItems, item, children, content, type, style, canHide, springConfig } = this.props

    var open = expandedOntologyItems[item.id];
    var selected = (this.props.selected) ? (this.props.selected.id == item.id) : false;

    const Icon =
      Icons[`${children ? (open ? 'Minus' : 'Plus') : 'Close'}SquareO`]

    return (
      <div style={{ ...styles.tree, ...style }} className="treeview">
        <Icon
          className="toggle"
          style={{ ...styles.toggle, opacity: children ? 1 : 0.3 }}
          onClick={this.toggle}
        />
        <span style={{ ...styles.type, marginRight: type ? 10 : 0 }}>
          {type}
        </span>
        {canHide && (
          <Icons.EyeO
            className="toggle"
            style={{ ...styles.toggle, opacity: visible ? 1 : 0.4 }}
            onClick={this.toggleVisibility}
          />
        )}



        <ContentWrapper 
          onClick={() => this.props.onSelect(item)} 
          selected={selected}
        > 

          {content}
          
         
          
        </ContentWrapper>
          
        {enableRemove && <IconButton 
                onClick={() => onRemove(item)}
                color="inherit" 
                size="small">
              <HighlightOffIcon />
            </IconButton>}
        {!enableRemove && <Placeholder/>}
   
        

        <Spring
          native
          immediate={immediate}
          config={{ ...config.default, precision: 0.1 }}
          from={{ height: 0, opacity: 0, transform: 'translate3d(20px,0,0)' }}
          to={{
            height: open ? 'auto' : 0,
            opacity: open ? 1 : 0,
            transform: open ? 'translate3d(0px,0,0)' : 'translate3d(20px,0,0)',
          }}
          {...springConfig && springConfig(open)}>
          {style => (
            <animated.div style={{ ...style, ...styles.contents }}>
              {children}
            </animated.div>
          )}
        </Spring>
      </div>
    )
  }
}
