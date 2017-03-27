/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package common.xnfc.card;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;

import java.io.IOException;
import java.util.ArrayList;

import common.xnfc.Converter;
import common.xnfc.card.pboc.PbocCard;
import common.xnfc.dataobject.mifare.MifareBlock;
import common.xnfc.dataobject.mifare.MifareClassCard;
import common.xnfc.dataobject.mifare.MifareSector;


public final class CardManager {
	private static final String SP = "<br/><img src=\"spliter\"/><br/>";

	public static String[][] TECHLISTS;
	public static IntentFilter[] FILTERS;

	static {
		try {
			TECHLISTS = new String[][] { { IsoDep.class.getName() },
					{ NfcV.class.getName() }, { NfcF.class.getName() }, };

			FILTERS = new IntentFilter[] { new IntentFilter(
					NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
		} catch (Exception e) {
		}
	}

	public static String buildResult(String n, String i, String d, String x) {
		if (n == null)
			return null;

		final StringBuilder s = new StringBuilder();

		s.append("<br/><b>").append(n).append("</b>");

		if (i != null)
			s.append(SP).append(i);

		if (d != null)
			s.append(SP).append(d);

		if (x != null)
			s.append(SP).append(x);

		return s.append("<br/><br/>").toString();
	}

	public static String load(Parcelable parcelable, Resources res,
			final MifareClassic mfc) {
		final Tag tag = (Tag) parcelable;

		final IsoDep isodep = IsoDep.get(tag);
		if (isodep != null) {
			return PbocCard.load(isodep, res);
		}

		final NfcV nfcv = NfcV.get(tag);
		if (nfcv != null) {
			return VicinityCard.load(nfcv, res);
		}

		final NfcF nfcf = NfcF.get(tag);
		if (nfcf != null) {
			return OctopusCard.load(nfcf, res);
		}

		if (mfc == null) {
			return null;
		}
		// 5.1) Connect to card

		try {
			mfc.close();
			mfc.connect();
			boolean auth = false;
			// 5.2) and get the number of sectors this card
			// has..and
			// loop
			// thru these sectors
			final int secCount = mfc.getSectorCount();
			System.out.println("-----secCount----" + secCount);
			MifareClassCard mifareClassCard = new MifareClassCard(secCount);
			int bCount = 0;
			int bIndex = 0;
			for (int j = 0; j < secCount; j++) {
				MifareSector mifareSector = new MifareSector();
				mifareSector.sectorIndex = j;
				// 6.1) authenticate the sector
				// auth = mfc.authenticateSectorWithKeyA(j,
				// "passwo".getBytes());
				auth = mfc.authenticateSectorWithKeyA(j,
						MifareClassic.KEY_DEFAULT);
				mifareSector.authorized = auth;
				System.out.println("-----auth" + auth);
				if (auth) {
					// 6.2) In each sector - get the block count
					bCount = mfc.getBlockCountInSector(j);
					bCount = Math.min(bCount, MifareSector.BLOCKCOUNT);
					bIndex = mfc.sectorToBlock(j);
					for (int i = 0; i < bCount; i++) {

						// 6.3) Read the block
						byte[] data = mfc.readBlock(bIndex);
						MifareBlock mifareBlock = new MifareBlock(data);
						mifareBlock.blockIndex = bIndex;
						// 7) Convert the data into a string
						// from Hex
						// format.

						bIndex++;
						mifareSector.blocks[i] = mifareBlock;

					}
					mifareClassCard.setSector(mifareSector.sectorIndex,
							mifareSector);
				} else { // Authentication failed - Handle it

				}
			}
			ArrayList<String> blockData = new ArrayList<String>();
			int blockIndex = 0;
			for (int i = 0; i < secCount; i++) {

				MifareSector mifareSector = mifareClassCard.getSector(i);
				for (int j = 0; j < MifareSector.BLOCKCOUNT; j++) {
					MifareBlock mifareBlock = mifareSector.blocks[j];
					byte[] data = mifareBlock.getData();
					blockData.add("Block "
							+ blockIndex++
							+ " : "
							+ Converter.getHexString(data,
									data.length));
					// System.out.println("1" + new
					// String(data));
				}
			}
//			String[] contents = new String[blockData.size()];
			String data = "";
			for (int i = 0; i < blockData.size(); i++) {
				data += blockData.get(i).toString();
			}
			mfc.close();
			return data;
		} catch (IOException e) {
			// Log.e(TAG, e.getLocalizedMessage());
		} finally {

		}

		return null;
	}
}
