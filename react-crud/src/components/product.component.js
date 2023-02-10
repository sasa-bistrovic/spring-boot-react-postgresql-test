import React, { Component } from "react";
import ProductDataService from "../services/product.service";

export default class Product extends Component {
  constructor(props) {
    super(props);
    this.onChangeCode = this.onChangeCode.bind(this);
    this.onChangeName = this.onChangeName.bind(this);
    this.onChangePricehrk = this.onChangePricehrk.bind(this);
    this.onChangePriceeur = this.onChangePriceeur.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.getProduct = this.getProduct.bind(this);
    this.updateisAvailable = this.updateisAvailable.bind(this);
    this.updateProduct = this.updateProduct.bind(this);
    this.deleteProduct = this.deleteProduct.bind(this);

    this.state = {
      currentProduct: {
        id: null,
        code: "",
        name: "",
        pricehrk: "",
        priceeur: "",
        description: "",
        isAvailable: false
      },
      eurconversion: "",
      message: ""
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

  componentDidMount() {
    this.getProduct(this.props.match.params.id);
  }

  onChangeCode(e) {
    const code = e.target.value;

    this.setState(function(prevState) {
      return {
        currentProduct: {
          ...prevState.currentProduct,
          code: code
        }
      };
    });
  }

  onChangeName(e) {
    const name = e.target.value;

    this.setState(function(prevState) {
      return {
        currentProduct: {
          ...prevState.currentProduct,
          name: name
        }
      };
    });
  }

  onChangePricehrk(e) {
    const pricehrk = e.target.value;

    let one = e.target.value;
    let two = this.state.eurconversion;
    let total = one / two;
    let stringeur = total.toFixed(2);

    this.setState(function(prevState) {
      return {
        currentProduct: {
          ...prevState.currentProduct,
          pricehrk: pricehrk,
          priceeur: stringeur
        }
      };
    });
  }

  onChangePriceeur(e) {
    const priceeur = e.target.value;

    this.setState(function(prevState) {
      return {
        currentProduct: {
          ...prevState.currentProduct,
          priceeur: priceeur
        }
      };
    });
  }

  onChangeDescription(e) {
    const description = e.target.value;
    
    this.setState(prevState => ({
      currentProduct: {
        ...prevState.currentProduct,
        description: description
      }
    }));
  }

  getProduct(id) {
    ProductDataService.get(id)
      .then(response => {
        this.setState({
          currentProduct: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  updateisAvailable(status) {
    let one = parseFloat(this.state.currentProduct.pricehrk);
    
    if (this.state.currentProduct.code.trim().length!=10) {
        this.setState({
           message: "Code (unique) (exactly 10 character) !"
        });
    return;
    }

    if ((this.state.currentProduct.pricehrk.trim() =="") || (this.state.currentProduct.pricehrk.trim().length==0) || (isNaN(one))) {
    one=0;
    }

    if (one<0) {
    one=0;
    }

    one=one.toFixed(2);

    let two = this.state.eurconversion;
    let total = one / two;
    let stringeur = total.toFixed(2);

    this.setState(function(prevState) {
      return {
        currentProduct: {
          ...prevState.currentProduct,
          pricehrk: one,
          priceeur: stringeur
        }
      };
    });

    var data = {
      id: this.state.currentProduct.id,
      code: this.state.currentProduct.code,
      name: this.state.currentProduct.name,
      pricehrk: one,
      priceeur: stringeur,
      description: this.state.currentProduct.description,
      isAvailable: status
    };

    ProductDataService.update(this.state.currentProduct.id, data)
      .then(response => {
        this.setState(prevState => ({
          currentProduct: {
            ...prevState.currentProduct,
            isAvailable: status
          }
        }));
                if (status) {
           	   this.setState({
                      message: "Product is available !"
                   });
		} else
                {
           	   this.setState({
                      message: "Product is not available !"
                   });
		}
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  updateProduct() {
    let one = parseFloat(this.state.currentProduct.pricehrk);

    if (this.state.currentProduct.code.trim().length!=10) {
        this.setState({
           message: "Code (unique) (exactly 10 character) !"
        });
        return;
    }

    if ((this.state.currentProduct.pricehrk.trim() =="") || (this.state.currentProduct.pricehrk.trim().length==0) || (isNaN(one))) {
    one=0;
    }

    if (one<0) {
    one=0;
    }

    one=one.toFixed(2);
    let two = this.state.eurconversion;
    let total = one / two;
    let stringeur = total.toFixed(2);

    this.setState(function(prevState) {
      return {
        currentProduct: {
          ...prevState.currentProduct,
          pricehrk: one,
          priceeur: stringeur
        }
      };
    });

    var data = {
      id: this.state.currentProduct.id,
      code: this.state.currentProduct.code,
      name: this.state.currentProduct.name,
      pricehrk: one,
      priceeur: stringeur,
      description: this.state.currentProduct.description,
      isAvailable: this.state.currentProduct.isAvailable
    };

    ProductDataService.update(this.state.currentProduct.id, data)
      .then(response => {
        this.setState(prevState => ({
          currentProduct: {
            ...prevState.currentProduct,
          }
        }));
        console.log(response.data);
        this.setState({
           message: "The product was updated successfully!"
        });
      })
      .catch(e => {
        console.log(e);
      });
  }

  deleteProduct() {    
    ProductDataService.delete(this.state.currentProduct.id)
      .then(response => {
        console.log(response.data);
        this.props.history.push('/products')
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { currentProduct } = this.state;

    return (
      <div>
        {currentProduct ? (
          <div className="edit-form">
            <h4>Product</h4>
            <form>
              <div className="form-group">
                <label htmlFor="code">Code</label>
                <input
                  type="text"
                  className="form-control"
                  id="code"
                  value={currentProduct.code}
                  onChange={this.onChangeCode}
                />
              </div>
              <div className="form-group">
                <label htmlFor="name">Name</label>
                <input
                  type="text"
                  className="form-control"
                  id="name"
                  value={currentProduct.name}
                  onChange={this.onChangeName}
                />
              </div>
              <div className="form-group">
                <label htmlFor="pricehrk">Pricehrk</label>
                <input
                  type="text"
                  className="form-control"
		  style={{ textAlign: 'right' }}
                  id="pricehrk"
                  value={currentProduct.pricehrk}
                  onChange={this.onChangePricehrk}
                />
              </div>
              <div className="form-group">
                <label htmlFor="priceeur">Priceeur</label>
                <input
                  type="text"
                  className="form-control"
                  style={{ textAlign: 'right' }}
                  id="priceeur"
                  value={currentProduct.priceeur}
                  onChange={this.onChangePriceeur}
                  disabled
                />
              </div>
              <div className="form-group">
                <label htmlFor="description">Description</label>
                <input
                  type="text"
                  className="form-control"
                  id="description"
                  value={currentProduct.description}
                  onChange={this.onChangeDescription}
                />
              </div>

              <div className="form-group">
                <label>
                  <strong>Status:</strong>
                </label>
                {currentProduct.isAvailable ? "isAvailable" : "Pending"}
              </div>
            </form>

            {currentProduct.isAvailable ? (
              <button
                className="badge badge-primary mr-2"
                onClick={() => this.updateisAvailable(false)}
              >
                IsNotAvailable
              </button>
            ) : (
              <button
                className="badge badge-primary mr-2"
                onClick={() => this.updateisAvailable(true)}
              >
                IsAvailable
              </button>
            )}

            <button
              className="badge badge-danger mr-2"
              onClick={this.deleteProduct}
            >
              Delete
            </button>

            <button
              type="submit"
              className="badge badge-success"
              onClick={this.updateProduct}
            >
              Update
            </button>
            <p>{this.state.message}</p>
          </div>
        ) : (
          <div>
            <br />
            <p>Please click on a Product...</p>
          </div>
        )}
      </div>
    );
  }
}
