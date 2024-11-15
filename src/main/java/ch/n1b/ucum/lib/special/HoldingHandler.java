/*******************************************************************************
BSD 3-Clause License

Copyright (c) 2006+, Health Intersections Pty Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 *******************************************************************************/

package ch.n1b.ucum.lib.special;

import ch.n1b.ucum.lib.Decimal;
import ch.n1b.ucum.lib.UcumException;

/**
 * If you want to actually use one of these units, then you'll
 * have to figure out how to implement them
 * 
 * @author Grahame Grieve
 *
 */
public class HoldingHandler extends SpecialUnitHandler {

	private String code;
	private String units;
	private Decimal value = Decimal.one();
	
	
	/**
	 * @param code
	 * @param units
	 */
	public HoldingHandler(String code, String units) {
		super();
		this.code = code;
		this.units = units;
	}

	public HoldingHandler(String code, String units, Decimal value) {
		super();
		this.code = code;
		this.units = units;
		this.value = value;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getUnits() {
		return units;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ohf.ucum.special.SpecialUnitHandler#getValue()
	 */
	@Override
	public Decimal getValue() {		
		return value;
	}

  /* (non-Javadoc)
   * @see org.eclipse.ohf.ucum.special.SpecialUnitHandler#getOffset()
   */
  @Override
  public Decimal getOffset() throws UcumException {   
    return new Decimal("0", 24);
  }

  @Override
  public boolean hasOffset() {
    return false;
  }
}
