package com.jsfcourse.calc;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
@Named
@RequestScoped
//@SessionScoped
public class CalcKredBB {
	private String ammount;
	private String years;
	private String interest;
	private Double result;

	@Inject
	FacesContext ctx;

	

	public String getAmmount() {
		return ammount;
	}

	public void setAmmount(String ammount) {
		this.ammount = ammount;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public boolean doTheMath() {
		try {
			double ammount = Double.parseDouble(this.ammount);
			double years = Double.parseDouble(this.years);
			double interest = Double.parseDouble(this.interest);
			
			result = (ammount+interest)/(years*12);

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacja wykonana poprawnie", null));
			return true;
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas przetwarzania parametrów", null));
			return false;
		}
	}

	// Go to "showresult" if ok
	public String calculate() {
		if (doTheMath()) {
			return "showresult";
		}
		return null;
	}
	
	// Put result in messages on AJAX call
    public String calc_AJAX() {
        if (doTheMath()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wynik: " + result, null));
        }
        return null;
    }

    public String info() {
        return "info";
    }

}
