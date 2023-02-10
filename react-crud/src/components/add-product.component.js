import React, { Component } from "react";
import ProductDataService from "../services/product.service";
import http from "../http-common";
import axios from 'axios';

export default class AddProduct extends Component {
  constructor(props) {
    super(props);
    this.onChangeCode = this.onChangeCode.bind(this);
    this.onChangeName = this.onChangeName.bind(this);
    this.onChangePricehrk = this.onChangePricehrk.bind(this);
    this.onChangePriceeur = this.onChangePriceeur.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.saveProduct = this.saveProduct.bind(this);
    this.newProduct = this.newProduct.bind(this);

    this.state = { 
      eurconversion: "",
      message: "",
      DataisLoaded: false,
      id: null,
      code: "",
      name: "",
      pricehrk: "",
      priceeur: "",
      description: "", 
      isAvailable: false,

      submitted: false
    };
    this.seteurconversion();   
  }

  seteurconversion () {
    fetch('../eur_conversion')
    .then((r) => r.text())
    .then(text  => {
      this.setState({
      eurconversion: text
    });
    })  
  } 

  onChangeCode(e) {
    this.setState({
      code: e.target.value
    });
  }

  onChangeName(e) {
    this.setState({
      name: e.target.value
    });
  }

  onChangePricehrk(e) {
    let one = e.target.value;
    let two = this.state.eurconversion;
    let total = one / two;
    //let stringhrk = ''+one;
    let stringeur = total.toFixed(2);
    this.setState({
      pricehrk: e.target.value,
      priceeur: stringeur
    });
  }

  onChangePriceeur(e) {
    //let stringeur = ''+e.target.value;
    this.setState({
      priceeur: e.target.value
    });
  }

  onChangeDescription(e) {
    this.setState({
      description: e.target.value
    });
  }
	
  saveProduct() {
    let one = parseFloat(this.state.pricehrk);

    if (this.state.code.trim().length!=10) {
        this.setState({
           message: "Code (unique) (exactly 10 character) !"
        });
    return;
    }

    if ((this.state.pricehrk.trim() =="") || (this.state.pricehrk.trim().length==0) || (isNaN(one))) {
    one=0;
    }
 
    if (one<0) {
    one=0;
    }

    one=one.toFixed(2);

    let two = this.state.eurconversion;
    let total = one / two;
    let stringeur = total.toFixed(2);

    this.setState({
      pricehrk: one,
      priceeur: stringeur
    });

    var data = {
      code: this.state.code,
      name: this.state.name,
      pricehrk: one,
      priceeur: stringeur,
      description: this.state.description
    };

    ProductDataService.create(data)
      .then(response => {
        this.setState({
          id: response.data.id,
          code: response.data.code,
          name: response.data.name,
          pricehrk: response.data.pricehrk,
          priceeur: response.data.priceeur,
          description: response.data.description,
          isAvailable: response.data.isAvailable,

          submitted: true
        });
        this.setState({
           message: ""
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  newProduct() {
    this.setState({
      id: null,
      code: "",
      name: "",
      pricehrk: "",
      priceeur: "",
      description: "",
      isAvailable: false,

      submitted: false
    });
  }

  render() {

      return (
      <div className="submit-form">
        {this.state.submitted ? (
          <div>
            <h4>You submitted successfully!</h4>
            <button className="btn btn-success" onClick={this.newProduct}>
              Add
            </button>
          </div>
        ) : (
	    <div>
            <div className="form-group">
              <label htmlFor="code">Code</label>
              <input
                type="text"
                className="form-control"
                id="code"
                required
                value={this.state.code}
                onChange={this.onChangeCode}
                name="code"
              />
            </div>
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                className="form-control"
                id="name"
                required
                value={this.state.name}
                onChange={this.onChangeName}
                name="name"
              />
            </div>
            <div className="form-group">
              <label htmlFor="pricehrk">Pricehrk</label>
              <input
                type="text"
                className="form-control"
                style={{ textAlign: 'right' }}
                id="pricehrk"
                required
                value={this.state.pricehrk}
                onChange={this.onChangePricehrk}
                name="pricehrk"
              />
            </div>
             <div className="form-group">
              <label htmlFor="priceeur">Priceeur</label>
              <input
                type="text"
                className="form-control"
                style={{ textAlign: 'right' }}
                id="priceeur"
                required
                value={this.state.priceeur}
                onChange={this.onChangePriceeur}
                name="priceeur"
		disabled
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Description</label>
              <input
                type="text"
                className="form-control"
                id="description"
                required
                value={this.state.description}
                onChange={this.onChangeDescription}
                name="description"
              />
            </div>

            <button onClick={this.saveProduct} className="btn btn-success">
              Submit
            </button>
            <p>{this.state.message}</p>
          </div>
        )}
      </div>
    );
  }
}
