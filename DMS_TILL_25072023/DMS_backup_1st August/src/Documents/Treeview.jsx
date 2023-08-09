import React from 'react'
import Tree from './TreeViewComponents/Tree'
import { useGlobalContext } from '../Context/DataContext'

const Treeview = () => {

     const {bookmarkOpen , checkOutOpen , nodeRefreshKey} = useGlobalContext();
    //  console.log(bookmarkOpen)
  
   return (
    <div className='shadow doc-height tree py-3 border border-secondary' style={{paddingTop:'30px',height:'43vw', flexGrow: 1}}>
       {bookmarkOpen ? <h1 style={{textAlign:"center"}}>BOOKMARK</h1> : checkOutOpen ? <h1 style={{textAlign:"center"}}>CHECKOUT</h1> : <Tree key={nodeRefreshKey}/> }
    </div> 
  )
}

export default Treeview